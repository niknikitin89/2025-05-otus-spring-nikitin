package ru.otus.hw.rest;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.CurrencyDto;
import ru.otus.hw.services.CurrencyService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService service;

    @GetMapping("/api/v1/currencies")
    public List<CurrencyDto> getAllCurrencies() {
        return service.findAll();
    }

    @GetMapping("/api/v1/currencies/{id}")
    public CurrencyDto getCurrencyById(@PathVariable long id) {
        return service.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Currency with id " + id + " not found"));
    }

    @PutMapping("/api/v1/currencies/{id}")
    public CurrencyDto updateCurrency(@PathVariable long id, @RequestBody CurrencyDto currencyDto) {
        currencyDto.setId(id);
        return service.save(currencyDto);
    }

    @PostMapping("/api/v1/currencies")
    public CurrencyDto createCurrency(@RequestBody CurrencyDto currencyDto) {
        return service.save(currencyDto);
    }

    @DeleteMapping("/api/v1/currencies/{id}")
    public void deleteCurrency(@PathVariable long id) {
        service.deleteById(id);
    }
}
