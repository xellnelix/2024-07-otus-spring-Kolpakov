package ru.otus.hw.services;


import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;
import static ru.otus.hw.mappers.BookMapper.bookDtoToBook;

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
    public BookDto findById(String id) {
        return BookMapper.bookToBookDto(bookRepository.findById(id).orElse(null));
    }

    @Transactional
    @Override
    public BookDto insert(BookDto bookDto) {
        var book = bookDtoToBook(bookDto);
        if (book.getAuthor() == null || book.getGenre() == null) {
            throw new EntityNotFoundException("Author or genre in book has not been found");
        }
        var author = authorRepository.findByFullName(book.getAuthor().getFullName())
                .orElseThrow(() -> new EntityNotFoundException("Author with name %s not found"
                        .formatted(book.getAuthor().getFullName()))
                );
        var genre = genreRepository.findByName(book.getGenre().getName())
                .orElseThrow(() -> new EntityNotFoundException("Genre with name %s not found"
                        .formatted(book.getGenre().getName()))
                );

        return BookMapper.bookToBookDto(bookRepository.save(book));
    }

    @Transactional
    @Override
    public BookDto update(BookDto bookDto) {
        var book = bookDtoToBook(bookDto);
        if (book.getAuthor() == null || book.getGenre() == null) {
            throw new EntityNotFoundException("Author or genre in book has not been found");
        }
        var author = authorRepository.findByFullName(book.getAuthor().getFullName())
                .orElseThrow(() -> new EntityNotFoundException("Author with name %s not found"
                        .formatted(book.getAuthor().getFullName()))
                );
        var genre = genreRepository.findByName(book.getGenre().getName())
                .orElseThrow(() -> new EntityNotFoundException("Genre with name %s not found"
                        .formatted(book.getGenre().getName())));
        var updatedBook = new Book(book.getId(), book.getTitle(), author, genre);
        return BookMapper.bookToBookDto(bookRepository.save(updatedBook));
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        bookRepository.deleteById(id);
    }
}