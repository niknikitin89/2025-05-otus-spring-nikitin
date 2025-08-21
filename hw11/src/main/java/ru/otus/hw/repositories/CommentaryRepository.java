package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import ru.otus.hw.models.Commentary;

public interface CommentaryRepository extends ReactiveMongoRepository<Commentary, String>
//        ,CustomCommentaryRepository
{

    @Nonnull
    Flux<Commentary> findAllByBookId(long id);

}
