package ru.otus.hw.page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.services.GenreService;

@Controller
@RequiredArgsConstructor
public class GenrePageController {

    private final GenreService genreService;

    //http://localhost:8080/all_genres
    @GetMapping("/all_genres")
    public String allGenresPage(Model model) {
        var genres = genreService.findAll();
        model.addAttribute("genres", genres);
        return "allGenresPage";
    }
}
