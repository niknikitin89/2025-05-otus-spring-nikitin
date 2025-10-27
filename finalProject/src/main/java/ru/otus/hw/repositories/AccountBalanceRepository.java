package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.models.Account;
import ru.otus.hw.models.AccountBalance;

import java.util.List;

public interface AccountBalanceRepository extends CrudRepository<AccountBalance, Integer> {

    @Override
    @EntityGraph(value = "account-balance-with-account")
    List<AccountBalance> findAll();

}
