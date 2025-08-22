package ru.otus.hw.page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class BookPageController {

    //http://localhost:8080/
    @GetMapping("/")
    public String allBooksPage() {
        return "allBooksPage";
    }

    //http://localhost:8080/book/1
    @GetMapping("/book")
    public String bookPage(@RequestParam("id") String id) {
        return "bookPage";
    }

    //http://localhost:8080/book/2/edit
    @GetMapping("/book/{id}/edit")
    public String editBookPage(@PathVariable String id) {
        return "bookEditPage";
    }

    @GetMapping("/add_book")
    String addBook() {
        return "bookEditPage";
    }

}
