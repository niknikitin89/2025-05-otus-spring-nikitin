package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.models.Account;
import ru.otus.hw.models.Currency;

import java.util.List;

public interface AccountRepository extends CrudRepository<Account, Integer> {

    @Override
    @EntityGraph(value = "account-with-bank-and-currency")
    List<Account> findAll();

}
