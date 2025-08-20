package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.repositories.BookRepository;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;

    @GetMapping("/api/v1/books")
    public Flux<BookDto> getAllBooks() {
        return bookRepository.findAllWithAuthorsAndGenres()
                .map(BookDto::fromDomainObject);
    }

    @GetMapping("/api/v1/books/{id}")
    public Mono<BookDto> getBook(@PathVariable long id) {
        return bookRepository.findByIdWithAuthorAndGenres(id).map(BookDto::fromDomainObject)
                .switchIfEmpty(Mono.error(
                        new EntityNotFoundException("Book with id " + id + " not found")
                ));
    }

    @PutMapping("/api/v1/books/{id}")
    public Mono<BookDto> updateBook(@PathVariable("id") long id, @RequestBody BookDto bookDto) {
        bookDto.setId(id);
        return bookRepository
                .saveBookWithAuthorsAndGenres(bookDto.toDomainObject())
                .map(BookDto::fromDomainObject);
    }

    @PostMapping("/api/v1/books")
    public Mono<BookDto> createBook(@RequestBody BookDto bookDto) {
        return bookRepository.saveBookWithAuthorsAndGenres(bookDto.toNewDomainObject())
                .map(BookDto::fromDomainObject);
    }

    @DeleteMapping("/api/v1/books/{id}")
    public Mono<Void> deleteBook(@PathVariable("id") long id) {
        return bookRepository.deleteById(id);
    }
}
