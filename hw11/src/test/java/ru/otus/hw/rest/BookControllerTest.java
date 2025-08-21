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
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.BookRepository;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(BookController.class)
class BookControllerTest {

//    @Autowired
//    private WebTestClient webTestClient;
//
//    @MockBean
//    private BookRepository bookRepository;
//
//    private Book book;
//    private BookDto bookDto;
//    private BookDto requestDto;
//
//    @BeforeEach
//    void setUp() {
//        Author author = new Author("1", "Author");
//        Genre genre = new Genre("1", "Genre");
//
//        book = new Book(
//                "1",
//                "Book",
//                author,
//                List.of(genre));
//
//        bookDto = BookDto.fromDomainObject(book);
//        requestDto = BookDto.fromDomainObject(book);
//    }
//
//    @Test
//    void shouldReturnAllBooks() {
//        // given
//        when(bookRepository.findAllWithAuthorsAndGenres())
//                .thenReturn(Flux.just(book));
//
//        // when & then
//        webTestClient.get()
//                .uri("/api/v1/books")
//                .exchange()
//                .expectStatus().isOk()
//                .expectBodyList(BookDto.class)
//                .hasSize(1)
//                .contains(bookDto);
//    }
//
//    @Test
//    void shouldReturnEmptyBooksList() {
//        // given
//        when(bookRepository.findAllWithAuthorsAndGenres())
//                .thenReturn(Flux.empty());
//
//        // when & then
//        webTestClient.get()
//                .uri("/api/v1/books")
//                .exchange()
//                .expectStatus().isOk()
//                .expectBodyList(BookDto.class)
//                .hasSize(0);
//    }
//
//    @Test
//    void shouldReturnBookById() {
//        // given
//        when(bookRepository.findByIdWithAuthorAndGenres(1L))
//                .thenReturn(Mono.just(book));
//
//        // when & then
//        webTestClient.get()
//                .uri("/api/v1/books/1")
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(BookDto.class)
//                .isEqualTo(bookDto);
//    }
//
//    @Test
//    void shouldReturnNotFoundForNonExistingBook() {
//        // given
//        when(bookRepository.findByIdWithAuthorAndGenres(999L))
//                .thenReturn(Mono.empty());
//
//        // when & then
//        webTestClient.get()
//                .uri("/api/v1/books/999")
//                .exchange()
//                .expectStatus().isNotFound();
//    }
//
//    @Test
//    void shouldCreateBook() {
//        // given
//        requestDto.setId(null);
//
//        when(bookRepository.saveBookWithAuthorsAndGenres(any(Book.class)))
//                .thenReturn(Mono.just(book));
//
//        // when & then
//        webTestClient.post()
//                .uri("/api/v1/books")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(requestDto)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(BookDto.class)
//                .isEqualTo(bookDto);
//    }
//
//    @Test
//    void shouldReturnBadRequestForInvalidBookCreation() {
//        // given
//        book.setAuthor(null);
//        requestDto = BookDto.fromDomainObject(book);
//
//        when(bookRepository.saveBookWithAuthorsAndGenres(any(Book.class)))
//                .thenReturn(Mono.error(new IllegalArgumentException("Author id must not be null")));
//
//        // when & then
//        webTestClient.post()
//                .uri("/api/v1/books")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(requestDto)
//                .exchange()
//                .expectStatus().isBadRequest();
//    }
//
//    @Test
//    void shouldUpdateBook() {
//
//        when(bookRepository.saveBookWithAuthorsAndGenres(any(Book.class)))
//                .thenReturn(Mono.just(book));
//
//        // when & then
//        webTestClient.put()
//                .uri("/api/v1/books/1")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(requestDto)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(BookDto.class)
//                .isEqualTo(bookDto);
//    }
//
//    @Test
//    void shouldReturnNotFoundForNonExistingBookOnUpdate() {
//        // given
//        requestDto.setId("999");
//
//        when(bookRepository.saveBookWithAuthorsAndGenres(any(Book.class)))
//                .thenReturn(Mono.error(new EntityNotFoundException("Book not found")));
//
//        // when & then
//        webTestClient.put()
//                .uri("/api/v1/books/999")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(requestDto)
//                .exchange()
//                .expectStatus().isNotFound();
//    }
//
//    @Test
//    void shouldDeleteBook() {
//        // given
//        when(bookRepository.deleteById("1")).thenReturn(Mono.empty());
//
//        // when & then
//        webTestClient.delete()
//                .uri("/api/v1/books/1")
//                .exchange()
//                .expectStatus().isOk();
//    }

}
