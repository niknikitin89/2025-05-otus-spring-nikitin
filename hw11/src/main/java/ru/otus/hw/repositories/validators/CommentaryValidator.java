package ru.otus.hw.repositories.validators;

import reactor.core.publisher.Mono;
import ru.otus.hw.projections.CommentaryProjection;

public interface CommentaryValidator {
    Mono<Void> validate(CommentaryProjection comment);

}
