package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.models.Bank;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankDto {

    private int id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isDeleted;

    public static BankDto fromDomainObject(Bank bank) {

        return new BankDto(bank.getId(), bank.getName(), bank.getCreatedAt(), bank.getUpdatedAt(), bank.getIsDeleted());
    }

    public Bank toDomainObject() {

        return new Bank(id, name, null, null, isDeleted);
    }
}
