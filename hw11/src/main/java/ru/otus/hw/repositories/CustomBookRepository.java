package ru.otus.hw.repositories;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Book;

public interface CustomBookRepository {

    Mono<Book> findByIdWithAuthorAndGenres(long id);

    Flux<Book> findAllWithAuthorsAndGenres();

    Mono<Book> saveBookWithAuthorsAndGenres(Book book);

}
