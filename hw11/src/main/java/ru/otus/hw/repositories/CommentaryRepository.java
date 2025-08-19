package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.otus.hw.models.Commentary;

import java.util.List;

public interface CommentaryRepository extends ReactiveCrudRepository<Commentary, Long>,
        CustomCommentaryRepository {

    @Nonnull
    Flux<Commentary> findAllByBookId(long id);

}
