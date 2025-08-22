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
import ru.otus.hw.dto.BookForCommentDto;
import ru.otus.hw.dto.CommentaryDto;
import ru.otus.hw.dto.CommentaryWithBookDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Commentary;
import ru.otus.hw.repositories.CommentaryRepository;

import static org.mockito.Mockito.when;

@WebFluxTest(CommentaryController.class)
class CommentaryControllerTest {
//    @Autowired
//    private WebTestClient webTestClient;
//
//    @MockBean
//    private CommentaryRepository commentaryRepository;
//
//    private Commentary commentary;
//
//    private CommentaryWithBookDto requestDtoWithBook;
//
//    private CommentaryDto expectedDto;
//
//    private CommentaryWithBookDto expectedDtoWithBook;
//
//    @BeforeEach
//    void setUp() {
//        Book book = new Book(
//                "1",
//                "Title",
//                new Author("1", "Author"),
//                null);
//
//        commentary = new Commentary("1", "Comment", book);
//        requestDtoWithBook = CommentaryWithBookDto.fromDomainObject(commentary);
//        expectedDto = CommentaryDto.fromDomainObject(commentary);
//        expectedDtoWithBook = CommentaryWithBookDto.fromDomainObject(commentary);
//    }
//
//    @Test
//    void shouldReturnCommentsForBook() {
//        // given
//        when(commentaryRepository.findAllByBook(1L))
//                .thenReturn(Flux.just(commentary));
//
//        // when & then
//        webTestClient.get()
//                .uri("/api/v1/books/1/comments")
//                .exchange()
//                .expectStatus().isOk()
//                .expectBodyList(CommentaryDto.class)
//                .hasSize(1)
//                .contains(expectedDto);
//    }
//
//    @Test
//    void shouldReturnEmptyCommentsForNonExistingBook() {
//        // given
//        when(commentaryRepository.findAllByBook(999L))
//                .thenReturn(Flux.empty());
//
//        // when & then
//        webTestClient.get()
//                .uri("/api/v1/books/999/comments")
//                .exchange()
//                .expectStatus().isOk()
//                .expectBodyList(CommentaryDto.class)
//                .hasSize(0);
//    }
//
//    @Test
//    void shouldReturnCommentWithBook() {
//        // given
//        when(commentaryRepository.findByIdWithBook(1L))
//                .thenReturn(Mono.just(commentary));
//
//        // when & then
//        webTestClient.get()
//                .uri("/api/v1/comments/1")
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(CommentaryWithBookDto.class)
//                .isEqualTo(expectedDtoWithBook);
//    }
//
//    @Test
//    void shouldReturnNotFoundForNonExistingComment() {
//        // given
//        when(commentaryRepository.findByIdWithBook(999L))
//                .thenReturn(Mono.empty());
//
//        // when & then
//        webTestClient.get()
//                .uri("/api/v1/comments/999")
//                .exchange()
//                .expectStatus().isNotFound();
//    }
//
//    @Test
//    void shouldAddComment() {
//        // given
//        when(commentaryRepository.save(commentary))
//                .thenReturn(Mono.just(commentary));
//
//        // when & then
//        webTestClient.post()
//                .uri("/api/v1/comments")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(requestDtoWithBook)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(CommentaryWithBookDto.class)
//                .isEqualTo(expectedDtoWithBook);
//    }
//
//    @Test
//    void shouldReturnBadRequestForEmptyComment() {
//        // given
//        requestDtoWithBook.setText("");
//
//        // when & then
//        webTestClient.post()
//                .uri("/api/v1/comments")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(requestDtoWithBook)
//                .exchange()
//                .expectStatus().isBadRequest();
//    }
//
//    @Test
//    void shouldReturnBadRequestForInvalidBookId() {
//        // given
//        requestDtoWithBook.setBook(new BookForCommentDto("0", "Test Book"));
//
//        // when & then
//        webTestClient.post()
//                .uri("/api/v1/comments")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(requestDtoWithBook)
//                .exchange()
//                .expectStatus().isBadRequest();
//    }
//
//    @Test
//    void shouldUpdateComment() {
//        // given
//        when(commentaryRepository.existsById("1")).thenReturn(Mono.just(true));
//        when(commentaryRepository.save(commentary))
//                .thenReturn(Mono.just(commentary));
//
//        // when & then
//        webTestClient.put()
//                .uri("/api/v1/comments/1")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(requestDtoWithBook)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(CommentaryWithBookDto.class)
//                .isEqualTo(requestDtoWithBook);
//    }
//
//    @Test
//    void shouldReturnBadRequestForZeroId() {
//        // when & then
//        webTestClient.put()
//                .uri("/api/v1/comments/0")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(requestDtoWithBook)
//                .exchange()
//                .expectStatus().isBadRequest();
//    }
//
//    @Test
//    void shouldReturnNotFoundForNonExistingCommentOnUpdate() {
//        // given
//        requestDtoWithBook.setId("999");
//
//        when(commentaryRepository.existsById("999")).thenReturn(Mono.just(false));
//
//        // when & then
//        webTestClient.put()
//                .uri("/api/v1/comments/999")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(requestDtoWithBook)
//                .exchange()
//                .expectStatus().isBadRequest();
//    }
//
//    @Test
//    void shouldDeleteComment() {
//        // given
//        when(commentaryRepository.deleteById("1")).thenReturn(Mono.empty());
//
//        // when & then
//        webTestClient.delete()
//                .uri("/api/v1/comments/1")
//                .exchange()
//                .expectStatus().isOk();
//    }

}