package ru.otus.hw.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.otus.hw.models.Bank;
import ru.otus.hw.models.Currency;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDto {

    private long id;

    private String code;

    private String name;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean isDeleted;

    public static CurrencyDto fromDomainObject(Currency currency) {

        return new CurrencyDto(currency.getId(),currency.getCode(),currency.getName(),
                currency.getCreatedAt(),currency.getUpdatedAt(),currency.getIsDeleted());
    }

    public Currency toDomainObject() {

        return new Currency(id, code, name, null, null, isDeleted);
    }
}
