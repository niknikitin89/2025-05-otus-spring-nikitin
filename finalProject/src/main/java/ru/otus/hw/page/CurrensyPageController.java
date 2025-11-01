package ru.otus.hw.page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class CurrensyPageController {

    //http://localhost:8080/currencies
    @GetMapping("/currencies")
    public String currencies(Model model) {
        return "currencies";
    }

}
