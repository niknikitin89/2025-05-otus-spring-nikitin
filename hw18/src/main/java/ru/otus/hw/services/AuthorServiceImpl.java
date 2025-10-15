package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    @Retry(name = "authors-service")
    @CircuitBreaker(name = "authors-service")
    public Optional<AuthorDto> findById(long id) {

        var authorOpt = authorRepository.findById(id);
        return authorOpt.map(AuthorDto::fromDomainObject);
    }

    @Override
    @Retry(name = "authors-service")
    @CircuitBreaker(name = "authors-service")
    public List<AuthorDto> findAll() {

        var authors = authorRepository.findAll();

        return authors.stream().map(AuthorDto::fromDomainObject).toList();
    }
}
