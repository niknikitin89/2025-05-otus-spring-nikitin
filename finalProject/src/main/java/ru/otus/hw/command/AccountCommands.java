package ru.otus.hw.command;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Account;
import ru.otus.hw.repositories.AccountRepository;

import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class AccountCommands {

    private final AccountRepository repository;

    @Transactional
    @ShellMethod(key={"find_all_accounts", "faa"}, value = "Find all accounts")
    public String findAll(){
        return repository.findAll().stream()
                .map(Account::toString)
                .collect(Collectors.joining(","+System.lineSeparator()));
    }
}
