package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Commentary;

public interface CommentaryService {
    Flux<Commentary> findAllByBookId(String bookId);

    Mono<Commentary> findByIdWithBook(String id);

    Mono<Commentary> saveComment(Commentary commentary);

    Mono<Void> deleteById(String id);
}
