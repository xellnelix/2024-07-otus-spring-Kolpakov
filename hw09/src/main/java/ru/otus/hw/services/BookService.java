package ru.otus.hw.services;


import java.util.List;
import ru.otus.hw.dto.BookDto;

public interface BookService {
    List<BookDto> findAll();

    BookDto insert(String title, String authorFullName, String genreName);

    BookDto update(long id, String title, String authorFullName, String genreName);

    void deleteById(long id);

    BookDto findById(long id);
}
