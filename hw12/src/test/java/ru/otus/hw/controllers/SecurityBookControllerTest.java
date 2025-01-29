package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.configuration.SecurityConfiguration;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Тесты безопасности")
@WebMvcTest(value = BookController.class)
@Import(SecurityConfiguration.class)
public class SecurityBookControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    private List<AuthorDto> dtoAuthors;

    private List<GenreDto> dtoGenres;

    private List<BookDto> dtoBooks;

    @BeforeEach
    void setUp() {
        dtoAuthors = getDtoAuthors();
        dtoGenres = getDtoGenres();
        dtoBooks = getDtoBooks(dtoAuthors, dtoGenres);
    }

    @Test
    @WithMockUser(username = "user", authorities = "user")
    void accessForUserTest() throws Exception {
        when(bookService.findAll()).thenReturn(dtoBooks);
        when(bookService.findById(anyLong())).thenReturn(dtoBooks.get(0));

        mvc.perform(get("/books")).andExpect(status().isOk());
        mvc.perform(get("/books/{id}", dtoBooks.get(0).getId()).with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", authorities = "user")
    void accessDeniedForUserTest() throws Exception {
        mvc.perform(post("/books/new")
                        .param("title", "NewBookTest")
                        .param("authorFullName", "Author_1")
                        .param("genreName", "Genre_1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", authorities = "admin")
    void accessForAdmin() throws Exception {
        BookDto expectedBook = new BookDto(4L, "NewBookTest", dtoAuthors.get(0), dtoGenres.get(0));
        given(bookService.findById(4)).willReturn(expectedBook);
        mvc.perform(post("/books/new")
                        .param("title", "NewBookTest")
                        .param("authorFullName", "Author_1")
                        .param("genreName", "Genre_1"))
                .andExpect(status().is3xxRedirection());
        mvc.perform(get("/books/4")).andExpect(status().isOk()).andExpect(model().attribute("book", expectedBook));
    }

    @Test
    public void unauthorizedRedirectTest() throws Exception{
        when(bookService.findAll()).thenReturn(dtoBooks);

        mvc.perform(get("/books"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "test")
    public void unauthorizedAccessDeniedTest() throws Exception{
        when(bookService.findAll()).thenReturn(dtoBooks);

        mvc.perform(get("/books"))
                .andExpect(status().isForbidden());
    }


    private static List<AuthorDto> getDtoAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new AuthorDto(id, "Author_" + id))
                .toList();
    }

    private static List<GenreDto> getDtoGenres() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new GenreDto(id, "Genre_" + id))
                .toList();
    }

    private static List<BookDto> getDtoBooks(List<AuthorDto> AuthorDtos, List<GenreDto> GenreDtos) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new BookDto(id, "BookTitle_" + id, AuthorDtos.get(id - 1), GenreDtos.get(id - 1)))
                .toList();
    }
}
