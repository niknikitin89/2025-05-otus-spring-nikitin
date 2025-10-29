package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.hibernate.query.criteria.JpaCriteriaInsertValues;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BalanceDto;
import ru.otus.hw.dto.BalanceForChangeDto;
import ru.otus.hw.dto.BankDto;
import ru.otus.hw.models.Account;
import ru.otus.hw.repositories.BalanceRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService {

    private final BalanceRepository repository;

    @Override
    public List<BalanceDto> findAll() {
        return repository.findAllByOrderByAccountNameAscBalanceDateDesc().stream()
                .map(BalanceDto::fromDomainObject)
                .toList();
    }

    @Override
    public Optional<BalanceDto> findByAccountAndDate(long accountId, LocalDate balanceDate) {

        var balanceOpt = repository.findByAccountAndBalanceDate(
                new Account(accountId), balanceDate);
        return balanceOpt.map(BalanceDto::fromDomainObject);
    }

    @Override
    public Optional<BalanceDto> findActualBalance(long accountId, LocalDate balanceDate) {
        var balanceOpt = repository.findActualBalanceOnDate(
                accountId, balanceDate);
        return balanceOpt.map(BalanceDto::fromDomainObject);
    }

    @Transactional
    @Override
    public BalanceDto save(BalanceForChangeDto balanceDto) {
        if (balanceDto.getAccountId() == 0 ||
                balanceDto.getBalanceDate() == null) {
            //TODO: перехватить
            throw new IllegalArgumentException("Balance date incorrect");
        }

        var now = LocalDateTime.now();
        var balance = balanceDto.toDomainObject();
        balance.setCreatedAt(now);

        try {
            var savedBalance = repository.save(balance);
            return BalanceDto.fromDomainObject(savedBalance);
        } catch (Exception e) {
            //TODO: перехватить
            throw new IllegalArgumentException("Error while saving");
        }
    }

}
