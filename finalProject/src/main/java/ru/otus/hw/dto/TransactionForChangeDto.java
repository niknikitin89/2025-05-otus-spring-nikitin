package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.models.Account;
import ru.otus.hw.models.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionForChangeDto {

    private long id;

    private BigDecimal amount;

    private Transaction.TransactionType type;

    private String description;

    private LocalDate transactionDate;

    private long accountId;

    public static TransactionForChangeDto fromDomainObject(Transaction tr) {

        return new TransactionForChangeDto(
                tr.getId(), tr.getAmount(), tr.getType(), tr.getDescription(),
                tr.getTransactionDate(), tr.getAccount().getId()
        );
    }

    public Transaction toDomainObject() {

        return new Transaction(id, amount, type, description, transactionDate,
                null, null, null,
                new Account(accountId));
    }
}
