package ru.otus.hw.services;

import ru.otus.hw.dto.BankDto;

import java.util.List;
import java.util.Optional;

public interface BankService {

    List<BankDto> findAll();

    Optional<BankDto> findById(int id);

    BankDto save(BankDto bankDto);

    void deleteById(int id);
}
