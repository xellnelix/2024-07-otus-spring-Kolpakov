package ru.otus.hw.repositories;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcBookRepository implements BookRepository {
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public JdbcBookRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcOperations = namedParameterJdbcTemplate;
    }

    @Override
    public Optional<Book> findById(long id) {
        try {
            return Optional.ofNullable(namedParameterJdbcOperations.queryForObject(
                    "select b.id as b_id, title, a.id as a_id, a.full_name, g.id as g_id, g.name from books b " +
                            "join authors a on b.author_id = a.id and b.id = :id " +
                            "join genres g on b.genre_id = g.id",
                    Map.of("id", id),
                    new BookRowMapper())
            );
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Book with id = " + id + " has not found");
        }
    }

    @Override
    public List<Book> findAll() {
        return namedParameterJdbcOperations.query(
                "select b.id as b_id, title, a.id as a_id, a.full_name, g.id as g_id, g.name from books b " +
                        "join authors a on b.author_id = a.id " +
                        "join genres g on b.genre_id = g.id",
                new BookRowMapper()
        );
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        int rowsDeleted = namedParameterJdbcOperations.update(
                "delete from books where id = :id",
                Map.of("id", id)
        );
        if (rowsDeleted == 0) {
            throw new EntityNotFoundException("Book with id = " + id + " has not found");
        }
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcOperations.update(
                "insert into books (title, author_id, genre_id) values (:title, :authorId, :genreId)",
                new MapSqlParameterSource().addValues(
                        Map.of(
                                "title", book.getTitle(),
                                "authorId", book.getAuthor().getId(),
                                "genreId", book.getGenre().getId()
                        )
                ),
                keyHolder);
        book.setId(keyHolder.getKeyAs(Long.class));
        return book;
    }

    private Book update(Book book) {
        int rowsUpdated = namedParameterJdbcOperations.update(
                "update books set title = :title, author_id = :authorId, genre_id = :genreId where id = :id",
                Map.of(
                        "id", book.getId(),
                        "title", book.getTitle(),
                        "authorId", book.getAuthor().getId(),
                        "genreId", book.getGenre().getId()
                )
        );
        if (rowsUpdated == 0) {
            throw new EntityNotFoundException("Book with id = " + book.getId() + " has not found");
        }
        return book;
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("b_id");
            String title = rs.getString("title");
            Author author = new Author(rs.getLong("a_id"), rs.getString("full_name"));
            Genre genre = new Genre(rs.getLong("g_id"), rs.getString("name"));
            return new Book(id, title, author, genre);
        }
    }
}
