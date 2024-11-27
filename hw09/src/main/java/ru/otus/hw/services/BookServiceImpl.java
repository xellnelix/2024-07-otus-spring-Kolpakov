package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream().map(BookMapper::bookToBookDto).collect(Collectors.toList());
    }

    @Override
    public BookDto insert(String title, String authorFullName, String genreName) {
        var author = authorRepository.findByFullName(authorFullName)
                .orElseThrow(() -> new EntityNotFoundException("Author with name %s not found".formatted(authorFullName)));
        var genre = genreRepository.findByName(genreName)
                .orElseThrow(() -> new EntityNotFoundException("Genre with name %s not found".formatted(genreName)));
        var book = new Book(title, author, genre);
        return BookMapper.bookToBookDto(bookRepository.save(book));
    }

    @Override
    public BookDto update(long id, String title, String authorFullName, String genreName) {
        var author = authorRepository.findByFullName(authorFullName)
                .orElseThrow(() -> new EntityNotFoundException("Author with name %s not found".formatted(authorFullName)));
        var genre = genreRepository.findByName(genreName)
                .orElseThrow(() -> new EntityNotFoundException("Genre with name %s not found".formatted(genreName)));
        var book = new Book(id, title, author, genre);
        return BookMapper.bookToBookDto(bookRepository.save(book));
    }

    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto findById(long id) {
        return BookMapper.bookToBookDto(bookRepository.findById(id).orElseThrow());
    }


}
