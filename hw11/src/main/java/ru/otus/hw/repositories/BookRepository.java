package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Book;

@Repository
public interface BookRepository extends ReactiveMongoRepository<Book, String> {
    Mono<Book> findById(String id);

    Flux<Book> findAll();

    Mono<Void> deleteById(String id);
}
