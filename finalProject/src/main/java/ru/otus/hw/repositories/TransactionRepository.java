package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.models.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

    @Override
    @EntityGraph(value = "transaction-with-account-basic")
    List<Transaction> findAll();

    @EntityGraph(value = "transaction-with-account-basic")
    Optional<Transaction> findById(long id);

    @EntityGraph(value = "transaction-with-account-basic")
    List<Transaction> findByAccountIdAndTransactionDate(long accountId, LocalDate transactionDate);

    @EntityGraph(value = "transaction-with-account-basic")
    List<Transaction> findByIsDeletedFalse();

}
