package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import ru.otus.hw.projections.BookProjection;

public interface BookRepository extends ReactiveMongoRepository<BookProjection, String> {

    @Nonnull
    @Override
    Flux<BookProjection> findAll();

}
