package ru.otus.hw.command;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.models.Bank;
import ru.otus.hw.repositories.BankRepository;

import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class BankCommands {

    private final BankRepository repository;

    @ShellMethod(key={"find_all_banks", "fab"}, value = "Find all banks")
    public String findAll(){
        return repository.findAll().stream()
                .map(Bank::toString)
                .collect(Collectors.joining(","+System.lineSeparator()));
    }
}
