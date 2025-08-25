package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import ru.otus.hw.models.Genre;

import java.util.List;

public interface GenreRepository extends ReactiveMongoRepository<Genre, String> {

    @Nonnull
    @Override
    Flux<Genre> findAll();

    Flux<Genre> findAllByIdIn(@Nonnull List<String> ids);

}
