package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    @CircuitBreaker(name = "default-service")
    public List<GenreDto> findAll() {

        return genreRepository.findAll().stream().map(GenreDto::fromDomainObject).toList();
    }
}
