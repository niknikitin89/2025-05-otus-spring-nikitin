package ru.otus.hw.repositories;

import reactor.core.publisher.Mono;
import ru.otus.hw.models.Commentary;

public interface CustomCommentaryRepository {

    Mono<Commentary> findByIdWithBook(Long id);
}
