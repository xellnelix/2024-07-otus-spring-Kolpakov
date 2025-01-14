package ru.otus.hw.services;


import java.util.List;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;

public interface BookService {
    List<BookDto> findAll();

    BookDto insert(Book book);

    BookDto update(long id, Book book);

    void deleteById(long id);

    BookDto findById(long id);
}
