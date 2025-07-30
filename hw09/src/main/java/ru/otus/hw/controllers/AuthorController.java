package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

@Controller
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    //http://localhost:8080/author?id=1
    //http://localhost:8080/author?id=2
    @GetMapping("/author")
    public String authorPage(@RequestParam("id")long id, Model model) {
        AuthorDto author = authorService.findById(id);
        model.addAttribute("author", author);
        return "authorPage";
    }

    //http://localhost:8080/all_authors
    @GetMapping("/all_authors")
    public String allAuthorsPage(Model model) {
        var authors = authorService.findAll();
        model.addAttribute("authors", authors);
        return "allAuthorsPage";
    }
}
