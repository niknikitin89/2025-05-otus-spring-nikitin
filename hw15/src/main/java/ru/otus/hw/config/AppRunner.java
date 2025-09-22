package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.otus.hw.services.DevelopmentService;

@Component
@RequiredArgsConstructor
public class AppRunner implements CommandLineRunner {

    private final DevelopmentService developmentService;

    @Override
    public void run(String... args) throws Exception {
        developmentService.startProgramming();
    }
}
