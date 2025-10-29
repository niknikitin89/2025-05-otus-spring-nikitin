package ru.otus.hw.validators;

import ru.otus.hw.dto.TransactionForChangeDto;

public interface TransactionValidator {

    public Boolean isValid(TransactionForChangeDto transaction);
}
