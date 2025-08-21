package ru.otus.hw.page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AuthorPageController {

    //http://localhost:8080/all_authors
    @GetMapping("/all_authors")
    public String allAuthorsPage() {
        return "allAuthorsPage";
    }
}
