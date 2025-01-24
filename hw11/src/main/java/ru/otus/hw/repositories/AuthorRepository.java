package ru.otus.hw.repositories;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

@Repository
public interface AuthorRepository extends MongoRepository<Author, Long> {
    Optional<Author> findByFullName(String fullName);
}
