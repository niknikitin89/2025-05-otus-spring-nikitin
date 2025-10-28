package ru.otus.hw.command;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.dto.CurrencyDto;
import ru.otus.hw.models.Currency;
import ru.otus.hw.repositories.CurrencyRepository;
import ru.otus.hw.services.CurrencyService;

import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class CurrencyCommands {

    private final CurrencyService service;

    //fac
    @ShellMethod(key = {"find_all_currencies", "fac"}, value = "Find all currencies")
    public String findAll() {

        return service.findAll().stream()
                .map(CurrencyDto::toString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    //fcid 4
    @ShellMethod(key = {"find_currency_by_id", "fcid"}, value = "Find currency by id")
    public String findById(int id) {

        return service.findById(id)
                .map(CurrencyDto::toString)
                .orElse("No such currency");
    }

    //dc 4
    @ShellMethod(key = {"delete", "dc"}, value = "Delete currency")
    public void deleteCurrency(int currencyId) {

        service.deleteById(currencyId);
    }

    //uc 2 "US" "Доллар"
    @ShellMethod(key = {"update_currency", "uc"}, value = "Update currency")
    public String updateCurrency(long currencyId, String code,String currencyName) {

        CurrencyDto currencyDto = new CurrencyDto();
        currencyDto.setId(currencyId);
        currencyDto.setCode(code);
        currencyDto.setName(currencyName);
        var updatedCurrency = service.save(currencyDto);
        return updatedCurrency.toString();
    }

    //cc "TG" "Тэнге"
    @ShellMethod(key = {"create_currency", "cc"}, value = "Create currency")
    public String createCurrency(String code, String currencyName) {

        CurrencyDto currencyDto = new CurrencyDto();
        currencyDto.setCode(code);
        currencyDto.setName(currencyName);
        var savedCurrency = service.save(currencyDto);
        return savedCurrency.toString();
    }
}
