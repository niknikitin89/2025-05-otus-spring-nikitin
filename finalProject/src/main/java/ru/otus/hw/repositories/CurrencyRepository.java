package ru.otus.hw.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.models.Bank;
import ru.otus.hw.models.Currency;

import java.util.List;
import java.util.Optional;

public interface CurrencyRepository extends CrudRepository<Currency, Integer> {

    @Override
    List<Currency> findAll();

    List<Currency> findAllByOrderByName();

    List<Currency> findByIsDeletedFalse();

    Optional<Currency> findById(long id);

    Optional<Currency> findByIdAndIsDeletedFalse(long id);

}
