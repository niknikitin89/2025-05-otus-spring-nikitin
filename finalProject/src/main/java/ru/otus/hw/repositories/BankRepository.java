package ru.otus.hw.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.models.Bank;

import java.util.List;
import java.util.Optional;

public interface BankRepository extends CrudRepository<Bank, Integer> {

    @Override
    List<Bank> findAll();

    List<Bank> findAllByOrderByName();

    List<Bank> findByIsDeletedFalse();

    Optional<Bank> findById(long id);

    Optional<Bank> findByIdAndIsDeletedFalse(long id);

}
