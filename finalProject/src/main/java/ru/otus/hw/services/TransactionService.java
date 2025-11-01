package ru.otus.hw.services;

import ru.otus.hw.dto.TransactionDto;
import ru.otus.hw.dto.TransactionForChangeDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionService {

    List<TransactionDto> findAll();

    Optional<TransactionDto> findById(long id);

    List<TransactionDto> findByAccountsAndDates(Long[] accountIds, LocalDate dateFrom, LocalDate dateTo);

    TransactionDto save(TransactionForChangeDto transactionDto);

    void deleteById(long id);

    void restoreById(long id);
}
