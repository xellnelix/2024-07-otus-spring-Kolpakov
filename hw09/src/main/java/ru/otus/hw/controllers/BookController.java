package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.services.BookService;

import java.util.List;

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

    @GetMapping("/newbook")
    public String createBookPage() {
        return "create";
    }

    @PostMapping("/newbook")
    public String createBook(@RequestParam(value = "title") String title, @RequestParam(value = "authorFullName") String authorFullName, @RequestParam(value = "genreName") String genreName) {
        bookService.insert(title, authorFullName, genreName);
        return "redirect:/books";
    }

    @GetMapping("/updatebook")
    public String updateBook(@RequestParam(value = "id") long id, Model model) {
        BookDto foundedBook = bookService.findById(id);
        model.addAttribute("book", foundedBook);
        return "edit";
    }

    @PostMapping("/updatebook")
    public String updateBook(@RequestParam(value = "id") long id, @RequestParam(value = "title") String title, @RequestParam(value = "authorFullName") String authorFullName,@RequestParam(value = "genreName") String genreName) {
        bookService.update(id, title, authorFullName, genreName);
        return "redirect:/books";
    }

    @GetMapping("/deletebook")
    public String deleteBook(@RequestParam(value = "id") long id) {
        bookService.deleteById(id);
        return "redirect:/books";
    }
}
