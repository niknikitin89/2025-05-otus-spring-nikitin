package ru.otus.hw.page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardPageController {

    //http://localhost:8080/dashboard
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "dashboard";
    }

}
