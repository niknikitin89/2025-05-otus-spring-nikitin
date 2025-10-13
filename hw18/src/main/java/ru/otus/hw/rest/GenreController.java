package ru.otus.hw.rest;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/api/v1/genres")
    @CircuitBreaker(name = "default-service", fallbackMethod = "getAllGenresFallback")
    public List<GenreDto> getAllGenres() {

        return genreService.findAll();
    }

    // Fallback метод
    private List<GenreDto> getAllGenresFallback(Exception e) {

        return Collections.emptyList();
    }
}
