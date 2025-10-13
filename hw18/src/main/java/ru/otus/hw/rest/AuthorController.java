package ru.otus.hw.rest;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/api/v1/authors")
    @Retry(name = "authors-service")
    @CircuitBreaker(name = "authors-service", fallbackMethod = "getAllAuthorsFallback")
    public List<AuthorDto> getAllAuthors() {

        return authorService.findAll();
    }


    // Fallback метод
    private List<AuthorDto> getAllAuthorsFallback(Exception e) {

        return Collections.emptyList();
    }
}
