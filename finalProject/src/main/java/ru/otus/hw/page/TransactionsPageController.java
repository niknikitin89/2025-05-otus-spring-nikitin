package ru.otus.hw.page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class TransactionsPageController {

    //http://localhost:8080/transactions
    @GetMapping("/transactions")
    public String transactions(Model model) {
        return "transactions";
    }

}
