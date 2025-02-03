package ru.otus.hw.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @EntityGraph(attributePaths = {"author", "genre"})
    Optional<Book> findById(long id);

    @EntityGraph(attributePaths = {"author", "genre"})
    List<Book> findAll();
}
