package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.models.Account;
import ru.otus.hw.models.Bank;
import ru.otus.hw.models.Currency;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountForChangeDto {

    private long id;

    private String name;

    private long bankId;

    private long currencyId;

    public static AccountForChangeDto fromDomainObject(Account account) {

        return new AccountForChangeDto(account.getId(), account.getName(),
                account.getBank().getId(),
                account.getCurrency().getId());
    }

    public Account toDomainObject() {

        Bank bank = new Bank();
        bank.setId(bankId);

        Currency currency = new Currency();
        currency.setId(currencyId);

        return new Account(id, name, null, null, null,
                bank, currency);
    }
}
