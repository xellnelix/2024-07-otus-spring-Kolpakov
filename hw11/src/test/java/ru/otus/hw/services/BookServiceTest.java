package ru.otus.hw.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;

@DisplayName("Сервис для работы с книгами")
@DataMongoTest
@Import({BookServiceImpl.class})
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
    @DisplayName("должен загружать книгу по id")
    void shouldReturnCorrectBookById() {
        var actualBook = bookService.findById("2");
        assertThat(actualBook).isEqualTo(dtoBooks.get(1));
    }

    @Test
    @DisplayName("должен загружать список всех книг")
    void shouldReturnCorrectBooksList() {
        var actualBook = bookService.findAll();
        assertThat(actualBook).isEqualTo(dtoBooks);
    }

    @Test
    @DisplayName("должен сохранять новую книгу")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldSaveNewBook() {
        var expectedBook = new BookDto("BookTitle_10500", dtoAuthors.get(0), dtoGenres.get(0));
        var actualBook = bookService.insert(expectedBook);
        assertThat(actualBook.getTitle()).isEqualTo(expectedBook.getTitle());
        assertThat(actualBook.getAuthor()).isEqualTo(expectedBook.getAuthor());
        assertThat(actualBook.getGenre()).isEqualTo(expectedBook.getGenre());
        assertNotNull(actualBook.getId());
    }

    @Test
    @DisplayName("должен сохранять измененную книгу")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldUpdateBook() {
        var bookBeforeUpdate = bookService.findById("1");
        var updatedBook = bookService.update(new BookDto("1", "Updated", new AuthorDto("1", "Author_1"), new GenreDto("1", "Genre_1")));
        assertThat(updatedBook).isNotEqualTo(bookBeforeUpdate);

        var bookAfterUpdate = bookService.findById("1");
        assertThat(bookAfterUpdate).isEqualTo(updatedBook);
    }

    @Test
    @DisplayName("должен удалять книгу по id")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldDeleteBook() {
        bookService.deleteById("1");
        assertThat(bookService.findById("1")).isNull();
    }

    @Test
    @DisplayName("Вернет empty Optional если указан несуществующий id книги")
    void shouldReturnEmpty() {
        assertNull(bookService.findById("50"));
    }


    private static List<AuthorDto> getDtoAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new AuthorDto(id.toString(), "Author_" + id))
                .toList();
    }

    private static List<GenreDto> getDtoGenres() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new GenreDto(id.toString(), "Genre_" + id))
                .toList();
    }

    private static List<BookDto> getDtoBooks(List<AuthorDto> dbAuthors, List<GenreDto> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new BookDto(id.toString(), "BookTitle_" + id, dbAuthors.get(id - 1), dbGenres.get(id - 1)))
                .toList();
    }

    private static List<BookDto> getDbtoBooks() {
        var dbAuthors = getDtoAuthors();
        var dbGenres = getDtoGenres();
        return getDtoBooks(dbAuthors, dbGenres);
    }
}
