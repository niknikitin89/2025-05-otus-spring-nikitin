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
public class BalanceDto {

    private long id;

    private AccountWithIdAndNameDto account;

    private LocalDate balanceDate;

    private BigDecimal amount;

    private LocalDateTime createdAt;

    public static BalanceDto fromDomainObject(Balance balance) {

        return new BalanceDto(balance.getId(),
                AccountWithIdAndNameDto.fromDomainObject(balance.getAccount()),
                balance.getBalanceDate(),
                balance.getAmount(), balance.getCreatedAt());
    }

    public Balance toDomainObject() {

        return new Balance(id, balanceDate, amount, null, account.toDomainObject());
    }
}
