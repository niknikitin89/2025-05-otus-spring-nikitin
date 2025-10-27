package ru.otus.hw.command;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.models.Currency;
import ru.otus.hw.repositories.CurrencyRepository;

import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class CurrencyCommands {

    private final CurrencyRepository repository;

    @ShellMethod(key={"find_all_currencies", "fac"}, value = "Find all currencies")
    public String findAll(){
        return repository.findAll().stream()
                .map(Currency::toString)
                .collect(Collectors.joining(","+System.lineSeparator()));
    }
}
