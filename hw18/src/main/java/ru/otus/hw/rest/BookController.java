package ru.otus.hw.rest;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.BookService;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/api/v1/books")
    @CircuitBreaker(name = "default-service", fallbackMethod = "getAllBooksFallback")
    @Retry(name = "default-service")
    public List<BookDto> getAllBooks() {

        return bookService.findAll();
    }

    @GetMapping("/api/v1/books/{id}")
    @CircuitBreaker(name = "default-service", fallbackMethod = "getBookFallback")
    @Retry(name = "default-service")
    public BookDto getBook(@PathVariable long id) {

        return bookService.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Book with id " + id + " not found")
                );
    }

    @PutMapping("/api/v1/books/{id}")
    @CircuitBreaker(name = "default-service")
    @Retry(name = "default-service")
    public BookDto updateBook(@PathVariable("id") long id, @RequestBody BookDto bookDto) {

        bookDto.setId(id);
        return bookService.save(bookDto);
    }

    @PostMapping("/api/v1/books")
    @CircuitBreaker(name = "default-service")
    @Retry(name = "default-service")
    public BookDto createBook(@RequestBody BookDto bookDto) {

        return bookService.save(bookDto);
    }

    @DeleteMapping("/api/v1/books/{id}")
    @CircuitBreaker(name = "default-service")
    @Retry(name = "default-service")
    public void deleteBook(@PathVariable("id") long id) {

        bookService.deleteById(id);
    }

    // FALLBACK МЕТОДЫ

    // Общий fallback для операций чтения
    public List<BookDto> getAllBooksFallback(Exception e) {

        return Collections.emptyList();
    }

    public BookDto getBookFallback(long id, Exception e) {

        System.err.println("Read operation fallback. Parameter: " + id + ", Error: " + e.getMessage());
        throw new EntityNotFoundException("Service unavailable. Unable to retrieve data for ID: " + id);
    }

}
