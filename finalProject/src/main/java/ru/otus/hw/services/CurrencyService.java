package ru.otus.hw.services;

import ru.otus.hw.dto.CurrencyDto;

import java.util.List;
import java.util.Optional;

public interface CurrencyService {

    List<CurrencyDto> findAll();

    Optional<CurrencyDto> findById(long id);

    CurrencyDto save(CurrencyDto currencyDto);

    void deleteById(long id);

    void restoreById(long id);
}
