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
import ru.otus.hw.dto.BalanceDto;
import ru.otus.hw.dto.BalanceForChangeDto;
import ru.otus.hw.services.BalanceService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceService service;

    @GetMapping("/api/v1/balances")
    public List<BalanceDto> getAllBalances() {
        return service.findAll();
    }

    @GetMapping("/api/v1/balances/{id}")
    public BalanceDto getBalanceById(@PathVariable long id) {
        return service.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Balance with id " + id + " not found"));
    }

    @GetMapping("/api/v1/accounts/{accountId}/balances")
    public List<BalanceDto> getBalancesByAccount(@PathVariable long accountId) {
        return service.findByAccountId(accountId);
    }

    @PutMapping("/api/v1/balances/{id}")
    public BalanceDto updateBalance(@PathVariable long id, @RequestBody BalanceForChangeDto balanceDto) {
        return null;
    }

    @PostMapping("/api/v1/balances")
    public BalanceDto createBalance(@RequestBody BalanceForChangeDto balanceDto) {
        return service.save(balanceDto);
    }

    @DeleteMapping("/api/v1/balances/{id}")
    public void deleteBalance(@PathVariable long id) {

    }
}
