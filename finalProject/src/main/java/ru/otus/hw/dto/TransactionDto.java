package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.models.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {

    private long id;

    private BigDecimal amount;

    private Transaction.TransactionType type;

    private String description;

    private LocalDate transactionDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean isDeleted;

    private AccountWithIdAndNameDto account;

    public static TransactionDto fromDomainObject(Transaction tr) {

        return new TransactionDto(
                tr.getId(), tr.getAmount(), tr.getType(), tr.getDescription(),
                tr.getTransactionDate(), tr.getCreatedAt(), tr.getUpdatedAt(), tr.getIsDeleted(),
                AccountWithIdAndNameDto.fromDomainObject(tr.getAccount())
        );
    }

    public Transaction toDomainObject() {

        return new Transaction(id, amount, type, description, transactionDate,
                null, null, isDeleted,
                account.toDomainObject());
    }
}
