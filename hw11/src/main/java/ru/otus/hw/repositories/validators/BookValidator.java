package ru.otus.hw.repositories.validators;

import reactor.core.publisher.Mono;
import ru.otus.hw.models.Book;
import ru.otus.hw.projections.BookProjection;

public interface BookValidator {

    Mono<Void> validateBook(BookProjection book);
}
