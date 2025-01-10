package ru.otus.hw.controllers;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.BookService;

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
    public BookDto createBook(@RequestParam(value = "title") String title,
                              @RequestParam(value = "authorFullName") String authorFullName,
                              @RequestParam(value = "genreName") String genreName
    ) {
        return bookService.insert(title, authorFullName, genreName);
    }

    @PutMapping("/books/{id}")
    public BookDto updateBook(@PathVariable("id") long bookId,
                              @RequestParam(value = "title") String title,
                              @RequestParam(value = "authorFullName") String authorFullName,
                              @RequestParam(value = "genreName") String genreName
    ) {
        return bookService.update(bookId, title, authorFullName, genreName);
    }

    @DeleteMapping("/books/{id}")
    public void deleteBook(@PathVariable("id") long bookId) {
        bookService.deleteById(bookId);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ModelAndView handleEntityNotFoundException(EntityNotFoundException e) {
        return new ModelAndView("redirect:/error");
    }
}
