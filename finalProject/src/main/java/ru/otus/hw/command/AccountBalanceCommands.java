package ru.otus.hw.command;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Account;
import ru.otus.hw.models.AccountBalance;
import ru.otus.hw.repositories.AccountBalanceRepository;
import ru.otus.hw.repositories.AccountRepository;

import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class AccountBalanceCommands {

    private final AccountBalanceRepository repository;

    @Transactional
    @ShellMethod(key={"find_all_account_balances", "faab"}, value = "Find all account balances")
    public String findAll(){
        return repository.findAll().stream()
                .map(AccountBalance::toString)
                .collect(Collectors.joining(","+System.lineSeparator()));
    }
}
