package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.repositories.GenreRepository;

@RestController
@RequiredArgsConstructor
public class GenreController {

    private final GenreRepository genreRepository;

    @GetMapping("/api/v1/genres")
    public Flux<GenreDto> getAllGenres() {

        return genreRepository.findAll()
                .sort((o1, o2) -> o1.getId().compareTo(o2.getId()))
                .map(GenreDto::fromDomainObject);
    }

}
