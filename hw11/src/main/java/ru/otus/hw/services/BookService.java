package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookWithAuthorAndGenresDto;
import ru.otus.hw.models.Book;

public interface BookService {
    Flux<BookWithAuthorAndGenresDto> findAllFullBooks();

    Mono<Book> findById(String id);

    Mono<BookWithAuthorAndGenresDto> findByIdFullBook(String id);

    Mono<BookWithAuthorAndGenresDto> saveBook(BookWithAuthorAndGenresDto book);

    Mono<Void> deleteById(String id);
}
