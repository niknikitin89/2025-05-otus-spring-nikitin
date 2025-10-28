package ru.otus.hw.command;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.dto.BankDto;
import ru.otus.hw.services.BankService;

import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class BankCommands {

    private final BankService service;

    //fab
    @ShellMethod(key = {"find_all_banks", "fab"}, value = "Find all banks")
    public String findAll() {

        return service.findAll().stream()
                .map(BankDto::toString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    //fbid 4
    @ShellMethod(key = {"find_bank_by_id", "fbid"}, value = "Find bank by id")
    public String findById(long id) {

        return service.findById(id)
                .map(BankDto::toString)
                .orElse("No such bank");
    }

    //db 4
    @ShellMethod(key = {"delete", "db"}, value = "Delete bank")
    public void deleteBank(long bankId) {

        service.deleteById(bankId);
    }

    //ub 2 "ВБРР"
    @ShellMethod(key = {"update_bank", "ub"}, value = "Update bank")
    public String updateBank(long bankId, String bankName) {

        BankDto bankDto = new BankDto();
        bankDto.setId(bankId);
        bankDto.setName(bankName);
        var updatedBank = service.save(bankDto);
        return updatedBank.toString();
    }

    //cb "Т-банк"
    @ShellMethod(key = {"create_bank", "cb"}, value = "Create bank")
    public String createBank(String bankName) {

        BankDto bankDto = new BankDto();
        bankDto.setName(bankName);
        var savedBank = service.save(bankDto);
        return savedBank.toString();
    }
}
