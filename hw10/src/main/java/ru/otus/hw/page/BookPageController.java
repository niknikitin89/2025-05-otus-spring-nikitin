package ru.otus.hw.page;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentaryService;
import ru.otus.hw.services.GenreService;

@Controller
@RequiredArgsConstructor
public class BookPageController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    private final CommentaryService commentaryService;

    //http://localhost:8080/
    @GetMapping("/")
    public String allBooksPage(Model model) {
        return "allBooksPage";
    }

    //http://localhost:8080/book/1
    @GetMapping("/book/{id}")
    public String bookPage(@PathVariable("id") long id, Model model) {
        var book = bookService.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Book with id " + id + " not found")
                );
        var commentary = commentaryService.findByBookId(id);
        model.addAttribute("book", book);
        model.addAttribute("commentary", commentary);
        return "bookPage";
    }

    //http://localhost:8080/book/2/edit
    @GetMapping("/book/{id}/edit")
    public String editBookPage(@PathVariable("id") long id, Model model) {
        var book = bookService.findById(id).orElseThrow(() -> new EntityNotFoundException("Book not found"));
        var authors = authorService.findAll();
        var genres = genreService.findAll();
        model.addAttribute("book", book);
        model.addAttribute("authors", authors);
        model.addAttribute("allGenres", genres);
        return "bookEditPage";
    }

    @PostMapping("/book/save")
    public String saveBook(@Valid @ModelAttribute BookDto book) {
        bookService.save(book);

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
