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
import ru.otus.hw.dto.BookWithAuthorAndGenresDto;
import ru.otus.hw.services.BookService;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/api/v1/books")
    public Flux<BookWithAuthorAndGenresDto> getAllBooks() {
        return bookService.findAllFullBooks();
    }

    @GetMapping("/api/v1/books/{id}")
    public Mono<BookWithAuthorAndGenresDto> getBook(@PathVariable String id) {
        return bookService.findByIdFullBook(id);
    }

    @PutMapping("/api/v1/books/{id}")
    public Mono<BookWithAuthorAndGenresDto> updateBook(
            @PathVariable("id") String id,
            @RequestBody BookWithAuthorAndGenresDto bookDto) {

        if (id == null || id.isBlank()) {
            return Mono.error(new IllegalArgumentException("Book id cannot be empty"));
        }

        bookDto.setId(id);
        return bookService.saveBook(bookDto);
    }

    @PostMapping("/api/v1/books")
    public Mono<BookWithAuthorAndGenresDto> createBook(
            @RequestBody BookWithAuthorAndGenresDto bookDto) {

        return bookService.saveBook(bookDto);
    }

    @DeleteMapping("/api/v1/books/{id}")
    public Mono<Void> deleteBook(@PathVariable("id") String id) {
        return bookService.deleteById(id);
    }
}
