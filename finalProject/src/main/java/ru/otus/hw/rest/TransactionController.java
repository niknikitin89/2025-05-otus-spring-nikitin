package ru.otus.hw.rest;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.BankDto;
import ru.otus.hw.dto.TransactionDto;
import ru.otus.hw.dto.TransactionForChangeDto;
import ru.otus.hw.models.Transaction;
import ru.otus.hw.services.BankService;
import ru.otus.hw.services.TransactionService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;

    @GetMapping("/api/v1/transactions")
    public List<TransactionDto> getAllTransactions() {
        return service.findAll();
    }

    @GetMapping("/api/v1/transactions/{id}")
    public TransactionDto getTransactionById(@PathVariable long id) {
        return service.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction with id " + id + " not found"));
    }

    @GetMapping("/api/v1/accounts/{accountId}/transactions")
    public List<TransactionDto> getTransactionsByAccount(@PathVariable long accountId) {
        return null;//service.findByAccountId(accountId);
    }

    @GetMapping("/api/v1/transactions/search")
    public List<TransactionDto> searchTransactions(
            @RequestParam(required = false) Long accountId,
            @RequestParam(required = false) Transaction.TransactionType type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return null;//service.findByCriteria(accountId, type, startDate, endDate);
    }

    @PutMapping("/api/v1/transactions/{id}")
    public TransactionDto updateTransaction(@PathVariable long id, @RequestBody TransactionForChangeDto transactionDto) {
        transactionDto.setId(id);
        return service.save(transactionDto);
    }

    @PostMapping("/api/v1/transactions")
    public TransactionDto createTransaction(@RequestBody TransactionForChangeDto transactionDto) {
        return service.save(transactionDto);
    }

    @DeleteMapping("/api/v1/transactions/{id}")
    public void deleteTransaction(@PathVariable long id) {
        service.deleteById(id);
    }

}
