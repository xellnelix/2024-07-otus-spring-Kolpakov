package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.BookMapper;
import static ru.otus.hw.mappers.BookMapper.bookDtoToBook;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

@RestController
@RequiredArgsConstructor
public class BookController {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @GetMapping("/books")
    public Flux<BookDto> getBooks() {
        return bookRepository.findAll().map(BookMapper::bookToBookDto);
    }

    @GetMapping("/books/{id}")
    public Mono<BookDto> findBook(@PathVariable("id") String bookId) {
        return bookRepository.findById(bookId).map(BookMapper::bookToBookDto);
    }

    @PostMapping("/books")
    public Mono<BookDto> createBook(@RequestBody BookDto bookDto) {
        var book = bookDtoToBook(bookDto);
        if (book.getAuthor() == null || book.getGenre() == null) {
            throw new EntityNotFoundException("Author or genre in book has not been found");
        }
        var author = getAuthorByFullName(book.getAuthor().getFullName());
        var genre = getGenreByName(book.getGenre().getName());
        return Mono.zip(author, genre,
                        (authorMono, genreMono) -> new Book(book.getTitle(), authorMono, genreMono))
                .flatMap(bookRepository::save)
                .map(BookMapper::bookToBookDto);
    }

    @PutMapping("/books/{id}")
    public Mono<BookDto> updateBook(@RequestBody BookDto bookDto) {
        var book = bookDtoToBook(bookDto);
        if (book.getAuthor() == null || book.getGenre() == null) {
            throw new EntityNotFoundException("Author or genre in book has not been found");
        }
        var author = getAuthorByFullName(book.getAuthor().getFullName());
        var genre = getGenreByName(book.getGenre().getName());
        return Mono.zip(author, genre,
                        (authorMono, genreMono) -> new Book(book.getId(), book.getTitle(), authorMono, genreMono))
                .flatMap(bookRepository::save)
                .map(BookMapper::bookToBookDto);
    }

    @DeleteMapping("/books/{id}")
    public Mono<Void> deleteBook(@PathVariable("id") String bookId) {
        return bookRepository.deleteById(bookId);
    }

    private Mono<Author> getAuthorByFullName(String fullName) {
        return authorRepository.findByFullName(fullName)
                .switchIfEmpty(Mono
                        .error(new EntityNotFoundException(String.format("Author with name %s not found", fullName))));
    }

    private Mono<Genre> getGenreByName(String name) {
        return genreRepository.findByName(name)
                .switchIfEmpty(Mono
                        .error(new EntityNotFoundException(String.format("Genre with name %s noy found", name))));
    }
}
