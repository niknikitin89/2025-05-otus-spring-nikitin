package ru.otus.hw.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.List;

import static org.mockito.Mockito.when;

@WebFluxTest(AuthorController.class)
class AuthorControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AuthorService authorService;

    @Test
    void getAllAuthorsShouldReturnAllAuthors() {
        // given
        List<AuthorDto> expectedAuthorsDto = List.of(
                new AuthorDto("1", "Author 1"),
                new AuthorDto("2", "Author 2")
        );

        when(authorService.getAllAuthors()).thenReturn(Flux.fromIterable(expectedAuthorsDto));

        // when & then
        webTestClient.get()
                .uri("/api/v1/authors")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AuthorDto.class)
                .hasSize(2)
                .contains(expectedAuthorsDto.toArray(new AuthorDto[0]));
    }

    @Test
    void getAllAuthorsShouldReturnEmptyListWhenNoAuthors() {
        // given
        when(authorService.getAllAuthors()).thenReturn(Flux.empty());

        // when & then
        webTestClient.get()
                .uri("/api/v1/authors")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AuthorDto.class)
                .hasSize(0);
    }

}
