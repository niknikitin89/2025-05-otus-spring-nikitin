package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Book;

public interface BookService {
    Flux<Book> findAllFullBooks();

    Mono<Book> findById(String id);

    Mono<Book> findByIdFullBook(String id);

    Mono<Book> saveBook(Book book);

    Mono<Void> deleteById(String id);
}
