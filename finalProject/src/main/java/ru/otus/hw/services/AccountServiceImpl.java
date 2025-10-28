package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.AccountDto;
import ru.otus.hw.dto.AccountDto;
import ru.otus.hw.dto.AccountForChangeDto;
import ru.otus.hw.models.Account;
import ru.otus.hw.models.Bank;
import ru.otus.hw.models.Currency;
import ru.otus.hw.repositories.AccountRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository repository;

    @Override
    public List<AccountDto> findAll() {
        return repository.findAllByOrderByName().stream()
                .map(AccountDto::fromDomainObject)
                .toList();
    }

    @Override
    public Optional<AccountDto> findById(long id) {

        var accountOpt = repository.findById(id);
        return accountOpt.map(AccountDto::fromDomainObject);
    }

    @Transactional
    @Override
    public AccountDto save(AccountForChangeDto accountDto) {
        if (accountDto.getId() == 0) {
            return insert(accountDto);
        }
        return update(accountDto);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        var accountOpt = repository.findById(id);
        if (accountOpt.isEmpty()) {
            throw new IllegalArgumentException("Account not found");
        }

        Account account = accountOpt.get();
        account.setIsDeleted(true);

        repository.save(account);
    }

    private AccountDto update(AccountForChangeDto accountDto) {

        var accountOpt = repository.findById(accountDto.getId());
        if (accountOpt.isEmpty()) {
            throw new IllegalArgumentException("Account not found");
        }

        Account account = accountOpt.get();
        account.setName(accountDto.getName());
        account.setBank(new Bank(accountDto.getBankId()));
        account.setCurrency(new Currency(accountDto.getCurrencyId()));
        account.setUpdatedAt(LocalDateTime.now());

        var updatedAccount = repository.save(account);

        return AccountDto.fromDomainObject(updatedAccount);
    }

    private AccountDto insert(AccountForChangeDto accountDto) {
        if (accountDto.getName().isEmpty() ||
                accountDto.getBankId() == 0 ||
                accountDto.getCurrencyId() == 0) {
            //TODO: перехватить
            throw new IllegalArgumentException("Account incorrect data");
        }

        var account = accountDto.toDomainObject();

        var now = LocalDateTime.now();

        account.setCreatedAt(now);
        account.setUpdatedAt(now);
        account.setIsDeleted(false);

        try {
            var savedAccount = repository.save(account);
            return AccountDto.fromDomainObject(savedAccount);
        } catch (Exception e) {
            //TODO: перехватить
            throw new IllegalArgumentException("Error while saving");
        }
    }
}
