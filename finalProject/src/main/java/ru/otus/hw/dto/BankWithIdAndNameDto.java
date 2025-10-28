package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.models.Bank;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankWithIdAndNameDto {

    private long id;
    private String name;

    public static BankWithIdAndNameDto fromDomainObject(Bank bank) {

        return new BankWithIdAndNameDto(bank.getId(), bank.getName());
    }

    public Bank toDomainObject() {

        return new Bank(id, name, null, null, null);
    }
}
