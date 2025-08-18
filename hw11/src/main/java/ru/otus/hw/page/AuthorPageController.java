package ru.otus.hw.page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.services.AuthorService;

@Controller
@RequiredArgsConstructor
public class AuthorPageController {

    //http://localhost:8080/all_authors
    @GetMapping("/all_authors")
    public String allAuthorsPage(Model model) {
        return "allAuthorsPage";
    }
}
