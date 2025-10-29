package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.otus.hw.models.Account;
import ru.otus.hw.models.Balance;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BalanceRepository extends CrudRepository<Balance, Integer> {

    @Override
    @EntityGraph("account-balance-with-account")
    List<Balance> findAll();

    @EntityGraph("account-balance-with-account")
    List<Balance> findAllByOrderByAccountNameAscBalanceDateDesc();

    @EntityGraph("account-balance-with-account")
    Optional<Balance> findById(long id);

    @EntityGraph("account-balance-with-account")
    Optional<Balance> findByAccountAndBalanceDate(Account account, LocalDate date);

    @Query(value = """
            select b from Balance b
            where b.account.id=:accountId
            and b.balanceDate<=:date
            order by b.balanceDate desc
            limit 1""")
    @EntityGraph("account-balance-with-account")
    Optional<Balance> findActualBalanceOnDate(
            @Param("accountId") long accountId,
            @Param("date") LocalDate date);
}
