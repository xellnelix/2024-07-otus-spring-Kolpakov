package ru.otus.hw.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

@Repository
public interface BookRepository extends MongoRepository<Book, Long> {
    Optional<Book> findById(String id);

    List<Book> findAll();

    void deleteById(String id);
}
