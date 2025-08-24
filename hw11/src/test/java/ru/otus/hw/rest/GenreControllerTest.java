package ru.otus.hw.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.List;

import static org.mockito.Mockito.when;

@WebFluxTest(GenreController.class)
class GenreControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GenreService genreService;

    @Test
    void getAllGenresShouldReturnAllGenres() {
        // given
        List<GenreDto> expectedGenresDto = List.of(
                new GenreDto("1", "Genre1"),
                new GenreDto("2", "Genre2"),
                new GenreDto("3", "Genre3")
        );

        when(genreService.getAllGenres()).thenReturn(Flux.fromIterable(expectedGenresDto));

        // when & then
        webTestClient.get()
                .uri("/api/v1/genres")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GenreDto.class)
                .hasSize(3)
                .contains(expectedGenresDto.toArray(new GenreDto[0]));
    }

    @Test
    void getAllGenresShouldReturnEmptyListWhenNoGenres() {
        // given
        when(genreService.getAllGenres()).thenReturn(Flux.empty());

        // when & then
        webTestClient.get()
                .uri("/api/v1/genres")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GenreDto.class)
                .hasSize(0);
    }
}