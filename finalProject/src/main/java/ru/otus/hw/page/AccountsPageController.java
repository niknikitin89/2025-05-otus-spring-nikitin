package ru.otus.hw.page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AccountsPageController {

    //http://localhost:8080/accounts
    @GetMapping("/accounts")
    public String accounts(Model model) {
        return "accounts";
    }

}
