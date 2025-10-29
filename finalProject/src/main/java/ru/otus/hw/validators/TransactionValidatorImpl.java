package ru.otus.hw.validators;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.TransactionForChangeDto;
import ru.otus.hw.repositories.TransactionRepository;

@Component
@NoArgsConstructor
public class TransactionValidatorImpl implements TransactionValidator {

    public Boolean isValid(TransactionForChangeDto transaction) {

        return transaction.getType() != null &&
                transaction.getAmount() != null &&
                transaction.getTransactionDate() != null &&
                transaction.getAccountId() != 0;
    }
}
