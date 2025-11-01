package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BankDto;
import ru.otus.hw.dto.CurrencyDto;
import ru.otus.hw.models.Bank;
import ru.otus.hw.models.Currency;
import ru.otus.hw.repositories.CurrencyRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository repository;

    @Override
    public List<CurrencyDto> findAll() {
        return repository.findAllByOrderByName().stream()
                .map(CurrencyDto::fromDomainObject)
                .toList();
    }

    @Override
    public Optional<CurrencyDto> findById(long id) {
        var currencyOpt = repository.findById(id);
        return currencyOpt.map(CurrencyDto::fromDomainObject);
    }

    @Transactional
    @Override
    public CurrencyDto save(CurrencyDto currencyDto) {
        if (currencyDto.getId() == 0) {
            return insert(currencyDto);
        }
        return update(currencyDto);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        var currencyOpt = repository.findById(id);
        if (currencyOpt.isEmpty()) {
            throw new IllegalArgumentException("Currency not found");
        }

        Currency currency = currencyOpt.get();
        currency.setIsDeleted(true);

        repository.save(currency);
    }

    @Override
    public void restoreById(long id) {
        var currencyOpt = repository.findById(id);
        if (currencyOpt.isEmpty()) {
            throw new IllegalArgumentException("Currency not found");
        }

        Currency currency = currencyOpt.get();
        currency.setIsDeleted(false);

        repository.save(currency);
    }

    private CurrencyDto update(CurrencyDto currencyDto) {

        var currencyOpt = repository.findById(currencyDto.getId());
        if (currencyOpt.isEmpty()) {
            throw new IllegalArgumentException("Currency not found");
        }

        Currency currency = currencyOpt.get();
        currency.setCode(currencyDto.getCode());
        currency.setName(currencyDto.getName());
        currency.setUpdatedAt(LocalDateTime.now());

        var updatedCurrency = repository.save(currency);

        return CurrencyDto.fromDomainObject(updatedCurrency);
    }

    private CurrencyDto insert(CurrencyDto currencyDto) {
        if (currencyDto.getName().isEmpty() || currencyDto.getCode().isEmpty()) {
            //TODO: перехватить
            throw new IllegalArgumentException("Currency data is empty");
        }

        var currency = currencyDto.toDomainObject();

        var now = LocalDateTime.now();

        currency.setCreatedAt(now);
        currency.setUpdatedAt(now);
        currency.setIsDeleted(false);

        try {
            var savedCurrency = repository.save(currency);
            return CurrencyDto.fromDomainObject(savedCurrency);
        }catch (Exception e) {
            //TODO: перехватить
            throw new IllegalArgumentException("Error while saving");
        }
    }

}
