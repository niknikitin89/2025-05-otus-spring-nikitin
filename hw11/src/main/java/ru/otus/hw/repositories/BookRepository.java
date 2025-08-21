package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Book;
import ru.otus.hw.projections.BookProjection;

public interface BookRepository extends ReactiveMongoRepository<BookProjection, String>
        , CustomBookRepository {

    @Nonnull
    @Override
    Flux<BookProjection> findAll();

}
