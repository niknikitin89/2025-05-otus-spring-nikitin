package ru.otus.hw.command;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.AccountDto;
import ru.otus.hw.dto.AccountForChangeDto;
import ru.otus.hw.dto.BankWithIdAndNameDto;
import ru.otus.hw.dto.CurrencyWithIdCodeNameDto;
import ru.otus.hw.models.Account;
import ru.otus.hw.repositories.AccountRepository;
import ru.otus.hw.services.AccountService;

import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class AccountCommands {

    private final AccountService service;

    //faa
    @ShellMethod(key = {"find_all_accounts", "faa"}, value = "Find all accounts")
    public String findAll() {

        return service.findAll().stream()
                .map(AccountDto::toString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    //faid 4
    @ShellMethod(key = {"find_account_by_id", "faid"}, value = "Find account by id")
    public String findById(int id) {

        return service.findById(id)
                .map(AccountDto::toString)
                .orElse("No such account");
    }

    //da 4
    @ShellMethod(key = {"delete", "da"}, value = "Delete account")
    public void deleteAccount(int accountId) {

        service.deleteById(accountId);
    }

    //ua 1 "My card" 2 2
    @ShellMethod(key = {"update_account", "ua"}, value = "Update account")
    public String updateAccount(long accountId, String accountName, long bankId, long currId) {
        AccountForChangeDto accountDto = new AccountForChangeDto();
        accountDto.setId(accountId);
        accountDto.setName(accountName);
        accountDto.setBankId(bankId);
        accountDto.setCurrencyId(currId);
        var updatedAccount = service.save(accountDto);
        return updatedAccount.toString();
    }

    //ca "My second card" 3 3
    @ShellMethod(key = {"create_account", "ca"}, value = "Create account")
    public String createAccount(String accountName, long bankId, long currId) {

        AccountForChangeDto accountDto = new AccountForChangeDto();
        accountDto.setName(accountName);
        accountDto.setBankId(bankId);
        accountDto.setCurrencyId(currId);
        var savedAccount = service.save(accountDto);
        return savedAccount.toString();
    }
}
