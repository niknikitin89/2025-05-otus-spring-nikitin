package ru.otus.hw.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.models.Bank;

import java.util.List;

public interface BankRepository extends CrudRepository<Bank, Integer> {

    @Override
    List<Bank> findAll();

}
