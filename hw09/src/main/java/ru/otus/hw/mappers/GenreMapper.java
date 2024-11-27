package ru.otus.hw.mappers;

import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Genre;

public class GenreMapper {
    public static GenreDto genreToGenreDto(Genre genre) {
        GenreDto genreDto = new GenreDto();
        genreDto.setId(genre.getId());
        genreDto.setName(genre.getName());
        return genreDto;
    }

    public static Genre genreDtoToGenre(GenreDto genreDto) {
        Genre genre = new Genre();
        genre.setId(genreDto.getId());
        genre.setName(genreDto.getName());
        return genre;
    }
}
