package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.otus.hw.services.SoftwareCreationService;

@Component
@RequiredArgsConstructor
public class AppRunner implements CommandLineRunner {

    private final SoftwareCreationService softwareCreationService;

    @Override
    public void run(String... args) throws Exception {
        softwareCreationService.start();
    }
}
