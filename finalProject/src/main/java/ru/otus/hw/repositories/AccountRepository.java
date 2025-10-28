package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.models.Account;
import ru.otus.hw.models.Bank;
import ru.otus.hw.models.Currency;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Integer> {

    @Override
    @EntityGraph(value = "account-with-bank-and-currency")
    List<Account> findAll();

    @EntityGraph(value = "account-with-bank-and-currency")
    List<Account> findAllByOrderByName();

    @EntityGraph(value = "account-with-bank-and-currency")
    List<Account> findByIsDeletedFalse();

    @EntityGraph(value = "account-with-bank-and-currency")
    Optional<Account> findById(long id);

    @EntityGraph(value = "account-with-bank-and-currency")
    Optional<Account> findByIdAndIsDeletedFalse(long id);
}
