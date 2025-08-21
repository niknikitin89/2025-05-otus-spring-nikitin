package ru.otus.hw.page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class GenrePageController {

    //http://localhost:8080/all_genres
    @GetMapping("/all_genres")
    public String allGenresPage() {
        return "allGenresPage";
    }
}
