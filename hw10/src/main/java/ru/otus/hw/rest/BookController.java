package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/api/book")
    public List<BookDto> getAllBooks(){
        return bookService.findAll();
    }

    @GetMapping("/api/book/{id}")
    public BookDto getBook(@PathVariable long id){
        BookDto book = bookService.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Book with id " + id + " not found")
                );

//        var commentary = commentaryService.findByBookId(id);
//        model.addAttribute("book", book);
//        model.addAttribute("commentary", commentary);
        return book;
    }


    @DeleteMapping("/api/book/{id}")
    public void deleteBook(@PathVariable("id") long id){
        bookService.deleteById(id);
    }
}
