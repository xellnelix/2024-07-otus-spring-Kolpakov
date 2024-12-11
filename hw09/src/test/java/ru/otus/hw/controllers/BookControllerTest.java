package ru.otus.hw.controllers;

import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.BDDMockito.given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.BookService;

@DisplayName("Тесты контроллера для работы с книгами")
@WebMvcTest(BookController.class)
public class BookControllerTest {
    @Autowired
    private MockMvc mvc;

    private List<AuthorDto> dtoAuthors;

    private List<GenreDto> dtoGenres;

    private List<BookDto> dtoBooks;

    @MockBean
    private BookService bookService;

    @BeforeEach
    void setUp() {
        dtoAuthors = getDtoAuthors();
        dtoGenres = getDtoGenres();
        dtoBooks = getDtoBooks(dtoAuthors, dtoGenres);
    }

    @Test
    public void readBookTest() throws Exception {
        given(bookService.findAll()).willReturn(dtoBooks);
        this.mvc.perform(get("/books")).andExpect(status().isOk()).andExpect(model().attribute("books", dtoBooks));
    }

    @Test
    public void readBookByIdTest() throws Exception {
        BookDto expectedBook = new BookDto(1L, "BookTitle_1", dtoAuthors.get(0), dtoGenres.get(0));
        given(bookService.findById(1)).willReturn(expectedBook);
        this.mvc.perform(get("/books/1")).andExpect(status().isOk()).andExpect(model().attribute("book", expectedBook));
    }

    @Test
    public void readBookByIdErrorTest() throws Exception {
        this.mvc.perform(get("/books/20")).andExpect(status().isOk()).andExpect(view().name("error"));
    }

    @Test
    public void readRedirectTest() throws Exception {
        this.mvc.perform(get("/")).andExpect(redirectedUrl("/books")).andExpect(status().is3xxRedirection());
    }

    @Test
    public void readNonexistentTest() throws Exception {
        this.mvc.perform(get("/nonexist")).andExpect(status().isNotFound());
    }

    @Test
    public void createBookTest() throws Exception {
        BookDto expectedBook = new BookDto(4L, "NewBookTest", dtoAuthors.get(0), dtoGenres.get(0));
        given(bookService.findById(4)).willReturn(expectedBook);
        this.mvc.perform(post("/books/new").param("title", "NewBookTest").param("authorFullName", "Author_1").param("genreName", "Genre_1")).andExpect(status().is3xxRedirection());
        this.mvc.perform(get("/books/4")).andExpect(status().isOk()).andExpect(model().attribute("book", expectedBook));
    }

    @Test
    public void updateBookTest() throws Exception {
        BookDto expectedBook = new BookDto(1L, "BookEdited", dtoAuthors.get(0), dtoGenres.get(0));
        given(bookService.update(1, "BookEdited", "Author_1", "Genre_1")).willReturn(expectedBook);
        given(bookService.findById(1)).willReturn(expectedBook);
        this.mvc.perform(post("/books/edit/1").param("title", "BookEdited").param("authorFullName", "Author_1").param("genreName", "Genre_1")).andExpect(status().is3xxRedirection());
        this.mvc.perform(get("/books/1")).andExpect(status().isOk()).andExpect(model().attribute("book", expectedBook));
    }

    @Test
    public void deleteBookTest() throws Exception {
        this.mvc.perform(post("/books/remove/1")).andExpect(status().is3xxRedirection());
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
