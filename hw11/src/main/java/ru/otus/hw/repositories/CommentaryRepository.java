package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import ru.otus.hw.models.Commentary;

public interface CommentaryRepository extends ReactiveCrudRepository<Commentary, Long>,
        CustomCommentaryRepository {

    @Nonnull
    Flux<Commentary> findAllByBookId(long id);

}
