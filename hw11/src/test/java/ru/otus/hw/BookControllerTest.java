package ru.otus.hw;

import java.util.List;
import java.util.stream.IntStream;
import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

@DisplayName("Тесты контроллера для работы с книгами")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureDataMongo
public class BookControllerTest {
    @MockBean
    BookRepository bookRepository;

    @MockBean
    AuthorRepository authorRepository;

    @MockBean
    GenreRepository genreRepository;

    @Autowired
    private WebTestClient webTestClient;

    private List<AuthorDto> dtoAuthors;

    private List<GenreDto> dtoGenres;

    private List<BookDto> dtoBooks;

    @BeforeEach
    void setUp() {
        dtoAuthors = getDtoAuthors();
        dtoGenres = getDtoGenres();
        dtoBooks = getDtoBooks(dtoAuthors, dtoGenres);
    }

    @Test
    public void readBookTest() throws Exception {
        given(bookRepository.findAll()).willReturn(Flux.fromIterable(dtoBooks).map(BookMapper::bookDtoToBook));

        var result = webTestClient.get().uri("/books")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(BookDto.class)
                .getResponseBody();

        var step = StepVerifier.create(result);
        StepVerifier.Step<BookDto> stepResult = null;
        for (var book : dtoBooks) {
            stepResult = step.expectNext(book);
        }
        assertThat(stepResult).isNotNull();
        stepResult.verifyComplete();

    }

    @Test
    public void readBookByIdTest() throws Exception {
        BookDto expectedBook = new BookDto("1", "BookTitle_1", dtoAuthors.get(0), dtoGenres.get(0));
        given(bookRepository.findById("1")).willReturn(Mono.just(BookMapper.bookDtoToBook(expectedBook)));

        var result = webTestClient.get().uri("/books/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(BookDto.class)
                .getResponseBody();

        var step = StepVerifier.create(result);
        StepVerifier.Step<BookDto> stepResult = step.expectNext(expectedBook);

        assertThat(stepResult).isNotNull();
        stepResult.verifyComplete();
    }

    @Test
    public void readNonexistentTest() throws Exception {
        var result = webTestClient.get().uri("/books/100")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    public void createBookTest() throws Exception {
        Book expectedBook = new Book(null, "NewBookTest", AuthorMapper.authorDtoToAuthor(dtoAuthors.get(0)), GenreMapper.genreDtoToGenre(dtoGenres.get(0)));

        given(genreRepository.findByName(anyString())).willReturn(Mono.just(GenreMapper.genreDtoToGenre(dtoGenres.get(0))));
        given(authorRepository.findByFullName(anyString())).willReturn(Mono.just(AuthorMapper.authorDtoToAuthor(dtoAuthors.get(0))));
        given(bookRepository.save(expectedBook)).willReturn(Mono.just(expectedBook));

        var result = webTestClient
                .post().uri("/books")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(expectedBook)
                .exchange()
                .expectStatus().isOk()
                .returnResult(BookDto.class)
                .getResponseBody();

        var step = StepVerifier.create(result);
        StepVerifier.Step<BookDto> stepResult = step.expectNext(BookMapper.bookToBookDto(expectedBook));

        Assertions.assertThat(stepResult).isNotNull();
        stepResult.verifyComplete();
    }

    @Test
    public void updateBookTest() throws Exception {
        BookDto expectedBook = new BookDto("1", "BookEdited", dtoAuthors.get(0), dtoGenres.get(0));

        given(genreRepository.findByName(anyString())).willReturn(Mono.just(GenreMapper.genreDtoToGenre(dtoGenres.get(0))));
        given(authorRepository.findByFullName(anyString())).willReturn(Mono.just(AuthorMapper.authorDtoToAuthor(dtoAuthors.get(0))));
        given(bookRepository.save(BookMapper.bookDtoToBook(expectedBook))).willReturn(Mono.just(BookMapper.bookDtoToBook(expectedBook)));

        var result = webTestClient
                .put().uri("/books/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(expectedBook)
                .exchange()
                .expectStatus().isOk()
                .returnResult(BookDto.class)
                .getResponseBody();

        var step = StepVerifier.create(result);
        StepVerifier.Step<BookDto> stepResult = step.expectNext(expectedBook);

        Assertions.assertThat(stepResult).isNotNull();
        stepResult.verifyComplete();
    }

    @Test
    public void deleteBookTest() throws Exception {
        var book = new BookDto("1", "BookTitle_1", dtoAuthors.get(0), dtoGenres.get(0));

        given(bookRepository.deleteById(book.getId())).willReturn(Mono.empty());

        var result = webTestClient
                .delete().uri("/books/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class)
                .getResponseBody();

        verify(bookRepository, times(1)).deleteById("1");
        assertThat(result).isNotNull();

    }

    @Test
    public void entityNotFoundExceptionTest() throws Exception {
        BookDto badDataBook = new BookDto("1", "BookEdited", new AuthorDto("5", "NotFoundAuthor"), dtoGenres.get(0));
        given(bookRepository.save(BookMapper.bookDtoToBook(badDataBook))).willThrow(EntityNotFoundException.class);

        var result = webTestClient
                .post().uri("/books")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .returnResult(String.class)
                .getResponseBody();

        assertThat(result).isNotNull();
    }

    private static List<AuthorDto> getDtoAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new AuthorDto(id.toString(), "Author_" + id))
                .toList();
    }

    private static List<GenreDto> getDtoGenres() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new GenreDto(id.toString(), "Genre_" + id))
                .toList();
    }

    private static List<BookDto> getDtoBooks(List<AuthorDto> AuthorDtos, List<GenreDto> GenreDtos) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new BookDto(id.toString(), "BookTitle_" + id, AuthorDtos.get(id - 1), GenreDtos.get(id - 1)))
                .toList();
    }
}
