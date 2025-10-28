package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.models.Account;
import ru.otus.hw.models.Balance;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceForChangeDto {

    private long accountId;

    private LocalDate balanceDate;

    private BigDecimal amount;

    public static BalanceForChangeDto fromDomainObject(Balance balance) {

        return new BalanceForChangeDto(
                balance.getAccount().getId(),
                balance.getBalanceDate(),
                balance.getAmount());
    }

    public Balance toDomainObject() {

        return new Balance(0L, balanceDate, amount, null,
                new Account(accountId));
    }
}
