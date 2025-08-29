package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.CommentaryDto;
import ru.otus.hw.dto.CommentaryWithBookDto;

public interface CommentaryService {
    Flux<CommentaryDto> findAllByBookId(String bookId);

    Mono<CommentaryWithBookDto> findByIdWithBook(String id);

    Mono<CommentaryWithBookDto> saveComment(CommentaryWithBookDto commentary);

    Mono<Void> deleteById(String id);
}
