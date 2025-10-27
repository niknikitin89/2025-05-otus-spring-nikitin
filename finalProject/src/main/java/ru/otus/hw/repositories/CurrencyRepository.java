package ru.otus.hw.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.models.Bank;
import ru.otus.hw.models.Currency;

import java.util.List;

public interface CurrencyRepository extends CrudRepository<Currency, Integer> {

    @Override
    List<Currency> findAll();

}
