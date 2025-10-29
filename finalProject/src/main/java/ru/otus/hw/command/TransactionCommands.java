package ru.otus.hw.command;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.shell.standard.ShellComponent;
//import org.springframework.shell.standard.ShellMethod;
//import ru.otus.hw.dto.TransactionDto;
//import ru.otus.hw.dto.TransactionForChangeDto;
//import ru.otus.hw.models.Transaction;
//import ru.otus.hw.services.TransactionService;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.stream.Collectors;
//
//@ShellComponent
//@RequiredArgsConstructor
//public class TransactionCommands {
//
//    private final TransactionService service;
//
//    //fat
//    @ShellMethod(key = {"find_all_transactions", "fat"}, value = "Find all transactions")
//    public String findAll() {
//
//        return service.findAll().stream()
//                .map(TransactionDto::toString)
//                .collect(Collectors.joining("," + System.lineSeparator()));
//    }
//
//    //ftid 4
//    @ShellMethod(key = {"find_transaction_by_id", "ftid"}, value = "Find transaction by id")
//    public String findById(long id) {
//
//        return service.findById(id)
//                .map(TransactionDto::toString)
//                .orElse("No such transaction");
//    }
//
//    //dt 4
//    @ShellMethod(key = {"delete", "dt"}, value = "Delete transaction")
//    public void deleteTransaction(long transactionId) {
//
//        service.deleteById(transactionId);
//    }
//
//    //ut 2 "TRANSFER" 2025-10-28 1000.50 1 "Перевод"
//    @ShellMethod(key = {"update_transaction", "ut"}, value = "Update transaction")
//    public String updateTransaction(long transactionId, Transaction.TransactionType type, LocalDate date,
//                                    BigDecimal amount, long accountId, String description) {
//
//        TransactionForChangeDto transactionDto = new TransactionForChangeDto();
//        transactionDto.setId(transactionId);
//        transactionDto.setType(type);
//        transactionDto.setTransactionDate(date);
//        transactionDto.setAmount(amount);
//        transactionDto.setDescription(description);
//        transactionDto.setAccountId(accountId);
//        var updatedTransaction = service.save(transactionDto);
//        return updatedTransaction.toString();
//    }
//
//    //ct "TRANSFER" 2025-10-28 1000.50 1 "Перевод"
//    @ShellMethod(key = {"create_transaction", "ct"}, value = "Create transaction")
//    public String createTransaction(Transaction.TransactionType type, LocalDate date,
//                                    BigDecimal amount, long accountId, String description) {
//
//        TransactionForChangeDto transactionDto = new TransactionForChangeDto();
//        transactionDto.setType(type);
//        transactionDto.setTransactionDate(date);
//        transactionDto.setAmount(amount);
//        transactionDto.setDescription(description);
//        transactionDto.setAccountId(accountId);
//        var savedTransaction = service.save(transactionDto);
//        return savedTransaction.toString();
//    }
//}
