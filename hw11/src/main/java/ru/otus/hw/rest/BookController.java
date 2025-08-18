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
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.services.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    @GetMapping("/api/v1/books")
    public Flux<BookDto> getAllBooks() {
        return bookRepository.findAllWithAuthorsAndGenres()
                .map(BookDto::fromDomainObject);
//                .map(book -> new BookDto(
//                        book.getId(),
//                        book.getTitle(),
//                        AuthorDto.fromDomainObject(book.getAuthor()),
//                        book.getGenres().stream().map(GenreDto::fromDomainObject).toList()));
//        return bookRepository.findAll()
//                .flatMap(
//                        book -> Mono.zip(
//                                        authorRepository.findById(book.getAuthorId())
//                                                .map(AuthorDto::fromDomainObject),
//                                        genreRepository.findByBookId(book.getId())
//                                                .map(GenreDto::fromDomainObject)
//                                                .collectList())
//                                .map(tuple -> {
//                                    AuthorDto author = tuple.getT1();
//                                    List<GenreDto> genres = tuple.getT2();
//                                    return new BookDto(
//                                            book.getId(),
//                                            book.getTitle(),
//                                            author,
//                                            genres);
//                                }));
    }

    @GetMapping("/api/v1/books/{id}")
    public Mono<BookDto> getBook(@PathVariable long id) {
        return bookRepository.findById(id).map(BookDto::fromDomainObject)
                .switchIfEmpty(Mono.error(
                        new EntityNotFoundException("Book with id " + id + " not found")
                ));
    }

    @PutMapping("/api/v1/books/{id}")
    public Mono<BookDto> updateBook(@PathVariable("id") long id, @RequestBody BookDto bookDto) {
        bookDto.setId(id);
        return bookRepository
                .save(bookDto.toDomainObject())
                .map(BookDto::fromDomainObject);
    }

    @PostMapping("/api/v1/books")
    public BookDto createBook(@RequestBody BookDto bookDto) {
        return bookService.save(bookDto);
    }

    @DeleteMapping("/api/v1/books/{id}")
    public void deleteBook(@PathVariable("id") long id) {
        bookService.deleteById(id);
    }
}
