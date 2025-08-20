package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import ru.otus.hw.models.Book;

public interface BookRepository extends ReactiveCrudRepository<Book, Long>, CustomBookRepository {

    @Nonnull
    @Override
    Flux<Book> findAll();

}
