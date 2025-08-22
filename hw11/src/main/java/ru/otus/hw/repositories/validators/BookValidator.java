package ru.otus.hw.repositories.validators;

import reactor.core.publisher.Mono;
import ru.otus.hw.projections.BookProjection;

public interface BookValidator {

    Mono<Void> validate(BookProjection book);
}
