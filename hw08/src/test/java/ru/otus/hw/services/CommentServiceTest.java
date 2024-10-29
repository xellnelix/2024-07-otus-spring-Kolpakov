package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис для работы с комментариями")
@DataJpaTest
@Import(value = {CommentServiceImpl.class,
        CommentConverter.class,
        BookServiceImpl.class,
        BookConverter.class,
        AuthorConverter.class,
        GenreConverter.class})
@Transactional(propagation = Propagation.NEVER)
public class CommentServiceTest {
    @Autowired
    private CommentService commentService;

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
    }

    @Test
    @DisplayName("должен загружать комментарий по id")
    void shouldReturnCorrectCommentById() {
        var expectedComment = new Comment(1, "Comment_1", dbBooks.get(0));
        var actualComment = commentService.findById(1);
        assertThat(actualComment).isPresent().get().isEqualTo(expectedComment);
    }

    @Test
    @DisplayName("должен загружать коментарии по id книги")
    void shouldReturnCorrectCommentList() {
        var expectedComments = List.of(
                new Comment(2, "Comment_2", dbBooks.get(1)),
                new Comment(3, "Comment_3", dbBooks.get(1))
        );
        var actualComments = commentService.findByBookId(2);
        assertThat(actualComments).isEqualTo(expectedComments);
    }

    @Test
    @DisplayName("должен сохранять новый комментарий")
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    void shouldSaveNewComment() {
        var expectedComment = new Comment(4, "Comment_4", dbBooks.get(2));
        var actualComment = commentService.insert("Comment_4", 3);
        assertThat(actualComment).isEqualTo(expectedComment);
    }

    @Test
    @DisplayName("должен сохранять измененный комментарий")
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    void shouldSaveUpdatedComment() {
        var commentBeforeUpdate = commentService.findById(1);
        var updatedComment = commentService.update(1, "UpdatedComment_1", 1);
        assertThat(commentBeforeUpdate).isNotEqualTo(updatedComment);

        var commentAfterUpdate = commentService.findById(1);
        assertThat(commentAfterUpdate).isPresent().get().isEqualTo(updatedComment);
    }

    @Test
    @DisplayName("должен удалять комментарий по id")
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    void shouldDeleteComment() {
        commentService.deleteById(1);
        assertThat(commentService.findById(1).isEmpty());
    }


    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(id, "BookTitle_" + id, dbAuthors.get(id - 1), dbGenres.get(id - 1)))
                .toList();
    }

    private static List<Book> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }
}
