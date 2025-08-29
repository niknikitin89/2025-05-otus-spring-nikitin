package ru.otus.hw.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookWithAuthorAndGenresDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.BookService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(BookController.class)
class BookControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BookService bookService;

    private Book book;
    private BookWithAuthorAndGenresDto bookDto;
    private BookWithAuthorAndGenresDto requestDto;

    @BeforeEach
    void setUp() {
        Author author = new Author("1", "Author");
        Genre genre = new Genre("1", "Genre");

        book = new Book(
                "1",
                "Book",
                author,
                List.of(genre));

        bookDto = BookWithAuthorAndGenresDto.fromDomainObject(book);
        requestDto = BookWithAuthorAndGenresDto.fromDomainObject(book);
    }

    @Test
    void shouldReturnAllBooks() {
        // given
        when(bookService.findAllFullBooks())
                .thenReturn(Flux.just(bookDto));

        // when & then
        webTestClient.get()
                .uri("/api/v1/books")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookWithAuthorAndGenresDto.class)
                .hasSize(1)
                .contains(bookDto);
    }

    @Test
    void shouldReturnEmptyBooksList() {
        // given
        when(bookService.findAllFullBooks())
                .thenReturn(Flux.empty());

        // when & then
        webTestClient.get()
                .uri("/api/v1/books")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookWithAuthorAndGenresDto.class)
                .hasSize(0);
    }

    @Test
    void shouldReturnBookById() {
        // given
        when(bookService.findByIdFullBook("1"))
                .thenReturn(Mono.just(bookDto));

        // when & then
        webTestClient.get()
                .uri("/api/v1/books/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookWithAuthorAndGenresDto.class)
                .isEqualTo(bookDto);
    }

    @Test
    void shouldReturnNotFoundForNonExistingBook() {
        // given
        when(bookService.findByIdFullBook("999"))
                .thenReturn(Mono.error(
                        new EntityNotFoundException("Book with id 999 not found")
                ));

        // when & then
        webTestClient.get()
                .uri("/api/v1/books/999")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void shouldCreateBook() {
        // given
        requestDto.setId(null);

        when(bookService.saveBook(any(BookWithAuthorAndGenresDto.class)))
                .thenReturn(Mono.just(bookDto));

        // when & then
        webTestClient.post()
                .uri("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookWithAuthorAndGenresDto.class)
                .isEqualTo(bookDto);
    }

    @Test
    void shouldUpdateBook() {

        when(bookService.saveBook(any(BookWithAuthorAndGenresDto.class)))
                .thenReturn(Mono.just(bookDto));

        // when & then
        webTestClient.put()
                .uri("/api/v1/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookWithAuthorAndGenresDto.class)
                .isEqualTo(bookDto);
    }

    @Test
    void shouldReturnNotFoundForNonExistingBookOnUpdate() {
        // given
        requestDto.setId("999");

        when(bookService.saveBook(any(BookWithAuthorAndGenresDto.class)))
                .thenReturn(Mono.error(new EntityNotFoundException("Book not found")));

        // when & then
        webTestClient.put()
                .uri("/api/v1/books/999")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void shouldDeleteBook() {
        // given
        when(bookService.deleteById("1")).thenReturn(Mono.empty());

        // when & then
        webTestClient.delete()
                .uri("/api/v1/books/1")
                .exchange()
                .expectStatus().isOk();
    }

}
