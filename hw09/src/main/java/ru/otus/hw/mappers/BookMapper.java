package ru.otus.hw.mappers;

import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;

import static ru.otus.hw.mappers.AuthorMapper.authorToAuthorDto;
import static ru.otus.hw.mappers.AuthorMapper.authorDtoToAuthor;
import static ru.otus.hw.mappers.GenreMapper.genreToGenreDto;
import static ru.otus.hw.mappers.GenreMapper.genreDtoToGenre;

public class BookMapper {

    public static BookDto bookToBookDto(Book book) {
        if (book == null)
            return null;

        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor() == null ? null : authorToAuthorDto(book.getAuthor()));
        bookDto.setGenre(book.getGenre() == null ? null : genreToGenreDto(book.getGenre()));
        return bookDto;
    }

    public static Book bookDtoToBook(BookDto bookDto) {
        if (bookDto == null)
            return null;

        Book book = new Book();
        book.setId(bookDto.getId());
        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor() == null ? null : authorDtoToAuthor(bookDto.getAuthor()));
        book.setGenre(bookDto.getGenre() == null ? null : genreDtoToGenre(bookDto.getGenre()));
        return book;
    }
}
