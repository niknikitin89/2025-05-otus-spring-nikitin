package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/api/v1/authors")
    public Flux<AuthorDto> getAllAuthors() {
        return authorService.getAllAuthors();
    }
}
