package ru.otus.hw.services;

import ru.otus.hw.dto.BalanceDto;
import ru.otus.hw.dto.BalanceForChangeDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BalanceService {

    List<BalanceDto> findAll();

    Optional<BalanceDto> findByAccountAndDate(long accountId, LocalDate balanceDate);

    Optional<BalanceDto> findActualBalance(long accountId, LocalDate balanceDate);

    BalanceDto save(BalanceForChangeDto balanceDto);
}
