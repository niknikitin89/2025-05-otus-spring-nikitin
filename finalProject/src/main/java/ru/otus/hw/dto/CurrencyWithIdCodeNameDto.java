package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.models.Currency;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyWithIdCodeNameDto {

    private long id;

    private String code;

    private String name;

    public static CurrencyWithIdCodeNameDto fromDomainObject(Currency currency) {

        return new CurrencyWithIdCodeNameDto(currency.getId(),currency.getCode(),currency.getName());
    }

    public Currency toDomainObject() {

        return new Currency(id, code, name, null, null, null);
    }
}
