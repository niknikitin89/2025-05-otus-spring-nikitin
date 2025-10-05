package ru.otus.hw.rest;

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

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/api/v1/books")
    public List<BookDto> getAllBooks() {
        return bookService.findAll();
    }

    @GetMapping("/api/v1/books/{id}")
    public BookDto getBook(@PathVariable long id) {
        return bookService.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Book with id " + id + " not found")
                );
    }

    @PutMapping("/api/v1/books/{id}")
    public BookDto updateBook(@PathVariable("id") long id, @RequestBody BookDto bookDto) {
        bookDto.setId(id);
        return bookService.save(bookDto);
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
