package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;

public interface BookService {
    Flux<Book> findAllFullBooks();
}
