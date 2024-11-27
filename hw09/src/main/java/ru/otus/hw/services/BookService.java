package ru.otus.hw.services;


import ru.otus.hw.dto.BookDto;

import java.util.List;

public interface BookService {
    List<BookDto> findAll();

    BookDto insert(String title, long authorId, long genreId);

    BookDto update(long id, String title, long authorId, long genreId);

    void deleteById(long id);

    BookDto findById(long id);
}
