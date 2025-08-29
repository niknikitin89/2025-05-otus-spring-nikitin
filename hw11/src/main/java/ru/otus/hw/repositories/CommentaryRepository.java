package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.projections.CommentaryProjection;

public interface CommentaryRepository extends ReactiveMongoRepository<CommentaryProjection, String> {
    @Nonnull
    Flux<CommentaryProjection> findAllByBook(String id);

    @Query(value = "{ 'book': ?0 }", delete = true)
    Mono<Void> deleteAllByBook(String bookId);
}
