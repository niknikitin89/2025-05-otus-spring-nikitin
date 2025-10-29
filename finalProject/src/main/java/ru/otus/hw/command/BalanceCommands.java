package ru.otus.hw.command;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.shell.standard.ShellComponent;
//import org.springframework.shell.standard.ShellMethod;
//import ru.otus.hw.dto.BalanceDto;
//import ru.otus.hw.dto.BalanceForChangeDto;
//import ru.otus.hw.services.BalanceService;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.stream.Collectors;
//
//@ShellComponent
//@RequiredArgsConstructor
//public class BalanceCommands {
//
//    private final BalanceService service;
//
//    //fabl
//    @ShellMethod(key = {"find_all_balances", "fabl"}, value = "Find all account balances")
//    public String findAll() {
//
//        return service.findAll().stream()
//                .map(BalanceDto::toString)
//                .collect(Collectors.joining("," + System.lineSeparator()));
//    }
//
//    //fblad 2 2024-02-01
//    @ShellMethod(key = {"find_balance_by_account_and_date", "fblad "}, value = "Find balance by account and date")
//    public String findById(long accountId, LocalDate date) {
//
//        return service.findByAccountAndDate(accountId, date)
//                .map(BalanceDto::toString)
//                .orElse("No such balance");
//    }
//
//    //factbl 2 2024-01-10
//    @ShellMethod(key = {"find_actual_balance", "factbl "}, value = "Find actual balance")
//    public String findActualBalance(long accountId, LocalDate date) {
//
//        return service.findActualBalance(accountId, date)
//                .map(BalanceDto::toString)
//                .orElse("No such balance");
//    }
//
//    //cbl 1 2025-10-28 1000.50
//    @ShellMethod(key = {"create_balance", "cbl"}, value = "Create balance")
//    public String createBalance(long accountId, LocalDate date, BigDecimal amount) {
//
//        BalanceForChangeDto balanceDto = new BalanceForChangeDto();
//        balanceDto.setAccountId(accountId);
//        balanceDto.setBalanceDate(date);
//        balanceDto.setAmount(amount);
//        var savedBalance = service.save(balanceDto);
//
//        return savedBalance.toString();
//    }
//
//}
