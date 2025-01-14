package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.stream.IntStream;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.otus.hw.mappers.BookMapper.bookDtoToBook;

@DisplayName("Тесты контроллера для работы с книгами")
@WebMvcTest(BookController.class)
public class BookControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

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
        this.mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(dtoBooks)));
    }

    @Test
    public void readBookByIdTest() throws Exception {
        BookDto expectedBook = new BookDto(1L, "BookTitle_1", dtoAuthors.get(0), dtoGenres.get(0));
        given(bookService.findById(1)).willReturn(expectedBook);
        this.mvc.perform(get("/books/1")).andExpect(status().isOk());
    }

    @Test
    public void readNonexistentTest() throws Exception {
        this.mvc.perform(get("/nonexist")).andExpect(status().isNotFound());
    }

    @Test
    public void createBookTest() throws Exception {
        BookDto expectedBook = new BookDto(4L, "NewBookTest", dtoAuthors.get(0), dtoGenres.get(0));
        given(bookService.findById(4)).willReturn(expectedBook);
        this.mvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(expectedBook)))
                .andExpect(status().isOk());
        this.mvc.perform(get("/books/4"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedBook)));
    }

    @Test
    public void updateBookTest() throws Exception {
        BookDto expectedBook = new BookDto(1L, "BookEdited", dtoAuthors.get(0), dtoGenres.get(0));
        given(bookService.update(1, new Book("BookEdited", new Author(1, "Author_1"), new Genre(1, "Genre_1")))).willReturn(expectedBook);
        given(bookService.findById(1)).willReturn(expectedBook);
        this.mvc.perform(put("/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(expectedBook)))
                .andExpect(status().isOk());
        this.mvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedBook)));
    }

    @Test
    public void deleteBookTest() throws Exception {
        this.mvc.perform(delete("/books/1")).andExpect(status().isOk());
        this.mvc.perform(get("/books/1")).andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void entityNotFoundExceptionTest() throws Exception {
        BookDto badDataBook = new BookDto(1L, "BookEdited", new AuthorDto(5, "NotFoundAuthor"), dtoGenres.get(0));
        given(bookService.update(1, bookDtoToBook(badDataBook))).willThrow(EntityNotFoundException.class);
        this.mvc.perform(put("/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(badDataBook)))
                .andExpect(status().isNotFound());
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
