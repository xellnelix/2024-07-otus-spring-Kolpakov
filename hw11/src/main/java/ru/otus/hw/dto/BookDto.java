package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookDto {
    private String id;

    private String title;

    private AuthorDto author;

    private GenreDto genre;

    public BookDto(String title, AuthorDto author, GenreDto genre) {
        this.title = title;
        this.author = author;
        this.genre = genre;
    }
}