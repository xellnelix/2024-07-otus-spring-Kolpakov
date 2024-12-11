package ru.otus.hw.controllers;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.services.BookService;

@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("/")
    public String redirect() {
        return "redirect:/books";
    }

    @GetMapping("/books")
    public String getBooks(Model model) {
        List<BookDto> books = bookService.findAll();
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/books/{id}")
    public String findBook(@PathVariable("id") long bookId, Model model) {
        BookDto foundBook = bookService.findById(bookId);

        if (foundBook == null) {
            return "error";
        }
        model.addAttribute("book", foundBook);
        return "foundbook";
    }

    @GetMapping("/books/new")
    public String createBookPage() {
        return "create";
    }

    @PostMapping("/books/new")
    public String createBook(@RequestParam(value = "title") String title,
                             @RequestParam(value = "authorFullName") String authorFullName,
                             @RequestParam(value = "genreName") String genreName
    ) {
        bookService.insert(title, authorFullName, genreName);
        return "redirect:/books";
    }

    @GetMapping("/books/edit/{id}")
    public String updateBook(@PathVariable("id") long bookId, Model model) {
        BookDto foundedBook = bookService.findById(bookId);
        model.addAttribute("book", foundedBook);
        return "edit";
    }

    @PostMapping("/books/edit/{id}")
    public String updateBook(@PathVariable("id") long bookId,
                             @RequestParam(value = "title") String title,
                             @RequestParam(value = "authorFullName") String authorFullName,
                             @RequestParam(value = "genreName") String genreName
    ) {
        bookService.update(bookId, title, authorFullName, genreName);
        return "redirect:/books";
    }

    @PostMapping("/books/remove/{id}")
    public String deleteBook(@PathVariable("id") long bookId) {
        bookService.deleteById(bookId);
        return "redirect:/books";
    }
}
