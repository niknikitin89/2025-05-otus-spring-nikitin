package ru.otus.hw.repositories.validators;

import reactor.core.publisher.Mono;
import ru.otus.hw.models.Book;

public interface BookValidator {

    Mono<Void> validateBook(Book book);
}
