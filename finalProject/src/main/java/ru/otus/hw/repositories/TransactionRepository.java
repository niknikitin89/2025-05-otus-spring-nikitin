package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.models.Account;
import ru.otus.hw.models.Transaction;

import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

    @Override
    @EntityGraph(value = "transaction-with-account-basic")
    List<Transaction> findAll();
}
