package ru.otus.hw.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
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

    @ChangeSet(order = "001", id = "dropDb", author = "xellnelix", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertAuthors", author = "xellnelix")
    public void insertAuthors(AuthorRepository authorRepository) {
        authors.put("A1", authorRepository.save(new Author("Author_1")));
        authors.put("A2", authorRepository.save(new Author("Author_2")));
        authors.put("A3", authorRepository.save(new Author("Author_3")));
    }

    @ChangeSet(order = "002", id = "insertGenres", author = "xellnelix")
    public void insertGenres(GenreRepository genreRepository) {
        genres.put("G1", genreRepository.save(new Genre("Genre_1")));
        genres.put("G2", genreRepository.save(new Genre("Genre_2")));
        genres.put("G3", genreRepository.save(new Genre("Genre_3")));
    }

    @ChangeSet(order = "003", id = "insertBooks", author = "xellnelix")
    public void insertBooks(BookRepository repository) {
        repository.save(new Book("BookTitle_1",
                authors.get("A1"),
                genres.get("G1")));
        repository.save(new Book("BookTitle_2",
                authors.get("A2"),
                genres.get("G2")));
        repository.save(new Book("BookTitle_3",
                authors.get("A3"),
                genres.get("G3")));
    }

    @ChangeSet(order = "004", id = "insertComments", author = "xellnelix")
    public void insertComments(BookRepository bookRepository, CommentRepository commentRepository) {
        List<Book> books = bookRepository.findAll();
        commentRepository.save(new Comment("CommentText_1", books.get(0)));
        commentRepository.save(new Comment("CommentText_2", books.get(1)));
        commentRepository.save(new Comment("CommentText_3", books.get(2)));
    }
}
