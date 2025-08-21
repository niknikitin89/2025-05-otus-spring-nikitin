package ru.otus.hw.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

import static org.mockito.Mockito.when;

@WebFluxTest(GenreController.class)
class GenreControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GenreRepository genreRepository;

    @Test
    void getAllGenresShouldReturnAllGenres() {
        // given
        List<Genre> genres = List.of(
                new Genre("1", "Genre1"),
                new Genre("2", "Genre2"),
                new Genre("3", "Genre3")
        );

        List<GenreDto> expectedDtos = genres.stream()
                .map(GenreDto::fromDomainObject)
                .toList();

        when(genreRepository.findAll()).thenReturn(Flux.fromIterable(genres));

        // when & then
        webTestClient.get()
                .uri("/api/v1/genres")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GenreDto.class)
                .hasSize(3)
                .contains(expectedDtos.toArray(new GenreDto[0]));
    }

    @Test
    void getAllGenresShouldReturnEmptyListWhenNoGenres() {
        // given
        when(genreRepository.findAll()).thenReturn(Flux.empty());

        // when & then
        webTestClient.get()
                .uri("/api/v1/genres")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GenreDto.class)
                .hasSize(0);
    }
}