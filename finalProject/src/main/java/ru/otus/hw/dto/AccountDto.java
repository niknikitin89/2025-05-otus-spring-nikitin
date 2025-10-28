package ru.otus.hw.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.otus.hw.models.Account;
import ru.otus.hw.models.Bank;
import ru.otus.hw.models.Currency;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    private long id;

    private String name;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean isDeleted;

    private BankWithIdAndNameDto bank;

    private CurrencyWithIdCodeNameDto currency;

    public static AccountDto fromDomainObject(Account account) {

        return new AccountDto(account.getId(), account.getName(),
                account.getCreatedAt(), account.getUpdatedAt(), account.getIsDeleted(),
                BankWithIdAndNameDto.fromDomainObject(account.getBank()),
                CurrencyWithIdCodeNameDto.fromDomainObject(account.getCurrency()));
    }

    public Account toDomainObject() {

        return new Account(id, name, null, null, isDeleted,
                bank.toDomainObject(), currency.toDomainObject());
    }
}
