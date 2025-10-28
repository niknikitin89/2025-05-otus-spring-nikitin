package ru.otus.hw.services;

import ru.otus.hw.dto.AccountDto;
import ru.otus.hw.dto.AccountForChangeDto;
import ru.otus.hw.dto.CurrencyDto;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    List<AccountDto> findAll();

    Optional<AccountDto> findById(long id);

    AccountDto save(AccountForChangeDto accountDto);

    void deleteById(long id);
}
