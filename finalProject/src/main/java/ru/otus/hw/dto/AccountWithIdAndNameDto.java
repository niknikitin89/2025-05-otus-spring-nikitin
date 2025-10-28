package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.models.Account;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountWithIdAndNameDto {

    private long id;

    private String name;

    public static AccountWithIdAndNameDto fromDomainObject(Account account) {

        return new AccountWithIdAndNameDto(account.getId(), account.getName());
    }

    public Account toDomainObject() {

        return new Account(id, name, null, null, null,
                null, null);
    }
}
