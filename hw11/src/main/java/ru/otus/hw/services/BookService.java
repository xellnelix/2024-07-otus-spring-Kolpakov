package ru.otus.hw.services;


import java.util.List;
import ru.otus.hw.dto.BookDto;

public interface BookService {
    List<BookDto> findAll();

    BookDto insert(BookDto book);

    BookDto update(BookDto book);

    void deleteById(String id);

    BookDto findById(String id);
}
