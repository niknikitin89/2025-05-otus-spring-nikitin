package ru.otus.hw.page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class BankPageController {

    //http://localhost:8080/banks
    @GetMapping("/banks")
    public String banks(Model model) {
        return "banks";
    }

}
