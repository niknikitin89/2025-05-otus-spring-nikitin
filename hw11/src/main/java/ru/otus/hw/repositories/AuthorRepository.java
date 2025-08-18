package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import ru.otus.hw.models.Author;

import java.util.List;

public interface AuthorRepository extends ReactiveCrudRepository<Author, Long> {
    @Override
    @Nonnull
    Flux<Author> findAll();

}
