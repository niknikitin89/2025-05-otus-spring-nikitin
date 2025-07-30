package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentaryService;
import ru.otus.hw.services.GenreService;

import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    private final CommentaryService commentaryService;

    //http://localhost:8080/
    @GetMapping("/")
    public String allBooksPage(Model model) {
        var books = bookService.findAll();
        model.addAttribute("books", books);
        return "allBooksPage";
    }

    //http://localhost:8080/book/1
    @GetMapping("/book/{id}")
    public String bookPage(@PathVariable("id") long id, Model model) {
        var book = bookService.findById(id);
        var commentary = commentaryService.findByBookId(id);
        model.addAttribute("book", book);
        model.addAttribute("commentary", commentary);
        return "bookPage";
    }

    //http://localhost:8080/book/2/edit
    @GetMapping("/book/{id}/edit")
    public String editBookPage(@PathVariable("id") long id, Model model) {
        var book = bookService.findById(id);
        var authors = authorService.findAll();
        var genres = genreService.findAll();
        model.addAttribute("book", book);
        model.addAttribute("authors", authors);
        model.addAttribute("allGenres", genres);
        return "bookEditPage";
    }

    @PostMapping("/book/save")
    public String saveBook(@ModelAttribute BookDto book) {
        if (book.getId() == 0) {
            bookService.insert(
                    book.getTitle(),
                    book.getAuthor().getId(),
                    book.getGenres().stream().map(Genre::getId).collect(Collectors.toSet()));
        } else {
                bookService.update(
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor().getId(),
                        book.getGenres().stream().map(Genre::getId).collect(Collectors.toSet()));

        }
        return "redirect:/";
    }

    @GetMapping("/add_book")
    String addBook(Model model) {
        BookDto book = new BookDto();
        var authors = authorService.findAll();
        var genres = genreService.findAll();
        model.addAttribute("book", book);
        model.addAttribute("authors", authors);
        model.addAttribute("allGenres", genres);
        return "bookEditPage";
    }
}
