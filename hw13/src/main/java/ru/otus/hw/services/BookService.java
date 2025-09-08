package ru.otus.hw.services;

import jakarta.validation.Valid;
import ru.otus.hw.dto.BookDto;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Optional<BookDto> findById(long id);

    List<BookDto> findAll();

    void deleteById(long id);

    BookDto save(@Valid BookDto book);
}
