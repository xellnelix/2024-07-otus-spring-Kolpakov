package ru.otus.hw.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ChangeLog
public class DatabaseChangelog {
    private final Map<String, Author> authors = new HashMap<>();

    private final Map<String, Genre> genres = new HashMap<>();

    @ChangeSet(order = "001", id = "insertAuthors", author = "xellnelix")
    public void insertAuthors(AuthorRepository authorRepository) {
        authors.put("A1", authorRepository.save(new Author("1","Author_1")));
        authors.put("A2", authorRepository.save(new Author("2","Author_2")));
        authors.put("A3", authorRepository.save(new Author("3","Author_3")));
    }

    @ChangeSet(order = "002", id = "insertGenres", author = "xellnelix")
    public void insertGenres(GenreRepository genreRepository) {
        genres.put("G1", genreRepository.save(new Genre("1","Genre_1")));
        genres.put("G2", genreRepository.save(new Genre("2","Genre_2")));
        genres.put("G3", genreRepository.save(new Genre("3","Genre_3")));
    }

    @ChangeSet(order = "003", id = "insertBooks", author = "xellnelix")
    public void insertBooks(BookRepository repository) {
        repository.save(new Book("1","BookTitle_1",
                authors.get("A1"),
                genres.get("G1")));
        repository.save(new Book("2","BookTitle_2",
                authors.get("A2"),
                genres.get("G2")));
        repository.save(new Book("3","BookTitle_3",
                authors.get("A3"),
                genres.get("G3")));
    }

    @ChangeSet(order = "004", id = "insertComments", author = "xellnelix")
    public void insertComments(BookRepository bookRepository, CommentRepository commentRepository) {
        List<Book> books = bookRepository.findAll();
        commentRepository.save(new Comment("1","CommentText_1", books.get(0)));
        commentRepository.save(new Comment("2","CommentText_2", books.get(1)));
        commentRepository.save(new Comment("3","CommentText_3", books.get(1)));
    }
}
