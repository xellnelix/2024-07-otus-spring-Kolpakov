package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.BookService;

import java.util.List;

import static ru.otus.hw.mappers.BookMapper.bookDtoToBook;

@RestController
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("/books")
    public List<BookDto> getBooks() {
        return bookService.findAll();
    }

    @GetMapping("/books/{id}")
    public BookDto findBook(@PathVariable("id") long bookId) {
        return bookService.findById(bookId);
    }

    @PostMapping("/books")
    public BookDto createBook(@RequestBody BookDto book) {
        return bookService.insert(bookDtoToBook(book));
    }

    @PutMapping("/books/{id}")
    public BookDto updateBook(@RequestBody BookDto book) {
        return bookService.update(bookDtoToBook(book));
    }

    @DeleteMapping("/books/{id}")
    public void deleteBook(@PathVariable("id") long bookId) {
        bookService.deleteById(bookId);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException() {
        return new ResponseEntity<>("Entity not found", HttpStatus.NOT_FOUND);
    }
}
