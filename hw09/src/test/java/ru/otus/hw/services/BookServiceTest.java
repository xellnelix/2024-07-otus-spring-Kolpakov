package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Тесты сервиса для работы с книгами")
@DataJpaTest
@Import(BookServiceImpl.class)
@Transactional(propagation = Propagation.NEVER)
public class BookServiceTest {
    @Autowired
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
    void shouldReturnCorrectBookById() {
        var actualBook = bookService.findById(2);
        assertThat(actualBook).isEqualTo(dtoBooks.get(1));
    }

    @Test
    void shouldReturnCorrectBooksList() {
        var actualBook = bookService.findAll();
        assertThat(actualBook).isEqualTo(dtoBooks);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldSaveNewBook() {
        var expectedBook = new BookDto(4L, "BookTitle_10500", dtoAuthors.get(0), dtoGenres.get(0));
        var actualBook = bookService.insert("BookTitle_10500", "Author_1", "Genre_1");
        assertThat(actualBook.getTitle()).isEqualTo(expectedBook.getTitle());
        assertThat(actualBook.getAuthor()).isEqualTo(expectedBook.getAuthor());
        assertThat(actualBook.getGenre()).isEqualTo(expectedBook.getGenre());
        assertNotNull(actualBook.getId());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldUpdateBook() {
        var bookBeforeUpdate = bookService.findById(1L);
        var updatedBook = bookService.update(1L, "Updated", "Author_1", "Genre_1");
        assertThat(updatedBook).isNotEqualTo(bookBeforeUpdate);

        var bookAfterUpdate = bookService.findById(1L);
        assertThat(bookAfterUpdate).isEqualTo(updatedBook);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldDeleteBook() {
        bookService.deleteById(1L);
        assertThat(bookService.findById(1L)).isNull();
    }

    @Test
    void shouldReturnEmpty() {
        assertThat(bookService.findById(50L)).isNull();
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
