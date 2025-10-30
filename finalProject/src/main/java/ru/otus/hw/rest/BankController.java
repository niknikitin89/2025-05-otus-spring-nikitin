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
import ru.otus.hw.dto.BankDto;
import ru.otus.hw.services.BankService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BankController {

    private final BankService service;

    @GetMapping("/api/v1/banks")
    public List<BankDto> getAllBanks() {

        return service.findAll();
    }

    @GetMapping("/api/v1/banks/{id}")
    public BankDto getBankById(@PathVariable long id) {

        return service.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Bank with id " + id + " not found")
                );
    }

    @PutMapping("/api/v1/banks/{id}")
    public BankDto updateBank(@PathVariable long id, @RequestBody BankDto bankDto) {

        bankDto.setId(id);
        return service.save(bankDto);
    }

    @PutMapping("/api/v1/banks/{id}/restore")
    public void restoreBank(@PathVariable long id) {

        service.restoreById(id);
    }

    @PostMapping("/api/v1/banks")
    public BankDto createBank(@RequestBody BankDto bankDto) {

        return service.save(bankDto);
    }

    @DeleteMapping("/api/v1/banks/{id}")
    public void deleteBank(@PathVariable long id) {

        service.deleteById(id);
    }
}
