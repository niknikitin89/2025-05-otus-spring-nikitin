package ru.otus.hw.rest;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.AccountDto;
import ru.otus.hw.dto.AccountForChangeDto;
import ru.otus.hw.dto.CurrencyDto;
import ru.otus.hw.services.AccountService;
import ru.otus.hw.services.CurrencyService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService service;

    @GetMapping("/api/v1/accounts")
    public List<AccountDto> getAllAccounts() {
        return service.findAll();
    }

    @GetMapping("/api/v1/accounts/{id}")
    public AccountDto getAccountById(@PathVariable long id) {
        return service.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account with id " + id + " not found"));
    }

    @PutMapping("/api/v1/accounts/{id}")
    public AccountDto updateAccount(@PathVariable long id, @RequestBody AccountForChangeDto accountDto) {
        accountDto.setId(id);
        return service.save(accountDto);
    }

    @PostMapping("/api/v1/accounts")
    public AccountDto createAccount(@RequestBody AccountForChangeDto accountDto) {
        return service.save(accountDto);
    }

    @DeleteMapping("/api/v1/accounts/{id}")
    public void deleteAccount(@PathVariable long id) {
        service.deleteById(id);
    }

}
