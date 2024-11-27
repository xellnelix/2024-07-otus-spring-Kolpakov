package ru.otus.hw.mappers;

import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.models.Author;

public class AuthorMapper {
    public static AuthorDto authorToAuthorDto(Author author) {
        if (author == null)
            return null;

        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(author.getId());
        authorDto.setFullName(author.getFullName());
        return  authorDto;
    }

    public static Author authorDtoToAuthor(AuthorDto authorDto) {
        if (authorDto == null)
            return null;

        Author author = new Author();
        author.setId(authorDto.getId());
        author.setFullName(authorDto.getFullName());
        return author;
    }
}
