package ru.otus.hw.repositories;

import reactor.core.publisher.Flux;
import ru.otus.hw.models.Book;

public interface CustomBookRepository {

    Flux<Book> findAllWithAuthorsAndGenres();
}
