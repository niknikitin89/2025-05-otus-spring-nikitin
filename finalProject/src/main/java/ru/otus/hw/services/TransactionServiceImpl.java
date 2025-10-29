package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.TransactionDto;
import ru.otus.hw.dto.TransactionForChangeDto;
import ru.otus.hw.models.Account;
import ru.otus.hw.models.Transaction;
import ru.otus.hw.repositories.TransactionRepository;
import ru.otus.hw.validators.TransactionValidator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;

    private final TransactionValidator validator;

    @Override
    public List<TransactionDto> findAll() {

        return repository.findAll().stream()
                .map(TransactionDto::fromDomainObject)
                .toList();
    }

    @Override
    public Optional<TransactionDto> findById(long id) {

        var transactOpt = repository.findById(id);
        return transactOpt.map(TransactionDto::fromDomainObject);
    }

    @Transactional
    @Override
    public TransactionDto save(TransactionForChangeDto transactionDto) {

        if (transactionDto.getId() == 0) {
            return insert(transactionDto);
        }
        return update(transactionDto);
    }

    @Override
    public void deleteById(long id) {

        var transactOpt = repository.findById(id);
        if (transactOpt.isEmpty()) {
            throw new IllegalArgumentException("Transaction not found");
        }

        Transaction transaction = transactOpt.get();
        transaction.setIsDeleted(true);

        repository.save(transaction);
    }

    private TransactionDto update(TransactionForChangeDto transactionDto) {

        var transactionOpt = repository.findById(transactionDto.getId());
        if (transactionOpt.isEmpty()) {
            throw new IllegalArgumentException("Transaction not found");
        }
        if (!validator.isValid(transactionDto)) {
            //TODO: перехватить
            throw new IllegalArgumentException("Incorrect transaction");
        }

        Transaction transaction = transactionOpt.get();
        transaction.setAmount(transactionDto.getAmount());
        transaction.setType(transactionDto.getType());
        transaction.setDescription(transactionDto.getDescription());
        transaction.setTransactionDate(transactionDto.getTransactionDate());
        transaction.setAccount(new Account(transactionDto.getAccountId()));
        transaction.setUpdatedAt(LocalDateTime.now());

        var updatedTransaction = repository.save(transaction);

        return TransactionDto.fromDomainObject(updatedTransaction);
    }

    private TransactionDto insert(TransactionForChangeDto transactionDto) {

        if (!validator.isValid(transactionDto)) {
            //TODO: перехватить
            throw new IllegalArgumentException("Incorrect transaction data");
        }

        var transaction = transactionDto.toDomainObject();

        var now = LocalDateTime.now();

        transaction.setCreatedAt(now);
        transaction.setUpdatedAt(now);
        transaction.setIsDeleted(false);

        try {
            var savedTransaction = repository.save(transaction);
            return TransactionDto.fromDomainObject(savedTransaction);
        } catch (Exception e) {
            //TODO: перехватить
            throw new IllegalArgumentException("Error while saving");
        }
    }
}
