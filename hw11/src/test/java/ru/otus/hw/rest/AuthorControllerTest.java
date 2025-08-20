package ru.otus.hw.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;

import static org.mockito.Mockito.when;

@WebFluxTest(AuthorController.class)
class AuthorControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AuthorRepository authorRepository;

    @Test
    void getAllAuthorsShouldReturnAllAuthors() {
        // given
        List<Author> authors = List.of(
                new Author(1L, "Author 1"),
                new Author(2L, "Author 2")
        );

        List<AuthorDto> expectedDtos = authors.stream()
                .map(AuthorDto::fromDomainObject)
                .toList();

        when(authorRepository.findAll()).thenReturn(Flux.fromIterable(authors));

        // when & then
        webTestClient.get()
                .uri("/api/v1/authors")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AuthorDto.class)
                .hasSize(2)
                .contains(expectedDtos.toArray(new AuthorDto[0]));
    }

    @Test
    void getAllAuthorsShouldReturnEmptyListWhenNoAuthors() {
        // given
        when(authorRepository.findAll()).thenReturn(Flux.empty());

        // when & then
        webTestClient.get()
                .uri("/api/v1/authors")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AuthorDto.class)
                .hasSize(0);
    }

}
