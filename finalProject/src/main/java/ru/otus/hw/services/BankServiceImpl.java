package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BankDto;
import ru.otus.hw.models.Bank;
import ru.otus.hw.repositories.BankRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {

    private final BankRepository repository;

    @Override
    public List<BankDto> findAll() {

        return repository.findAllByOrderByName().stream()
                .map(BankDto::fromDomainObject)
                .toList();
    }

    @Override
    public Optional<BankDto> findById(long id) {

        var bankOpt = repository.findById(id);
        return bankOpt.map(BankDto::fromDomainObject);
    }

    @Transactional
    @Override
    public BankDto save(BankDto bankDto) {

        if (bankDto.getId() == 0) {
            return insert(bankDto);
        }
        return update(bankDto);
    }

    @Transactional
    @Override
    public void deleteById(long id) {

        var bankOpt = repository.findById(id);
        if (bankOpt.isEmpty()) {
            throw new IllegalArgumentException("Bank not found");
        }

        Bank bank = bankOpt.get();
        bank.setIsDeleted(true);

        repository.save(bank);

    }

    @Override
    public void restoreById(long id) {
        var bankOpt = repository.findById(id);
        if (bankOpt.isEmpty()) {
            throw new IllegalArgumentException("Bank not found");
        }

        Bank bank = bankOpt.get();
        bank.setIsDeleted(false);

        repository.save(bank);
    }

    private BankDto update(BankDto bankDto) {

        var bankOpt = repository.findById(bankDto.getId());
        if (bankOpt.isEmpty()) {
            throw new IllegalArgumentException("Bank not found");
        }

        Bank bank = bankOpt.get();
        bank.setName(bankDto.getName());
        bank.setUpdatedAt(LocalDateTime.now());

        var updatedBank = repository.save(bank);

        return BankDto.fromDomainObject(updatedBank);
    }

    private BankDto insert(BankDto bankDto) {

        if (bankDto.getName().isEmpty()) {
            //TODO: перехватить
            throw new IllegalArgumentException("Bank name is empty");
        }

        var bank = bankDto.toDomainObject();

        var now = LocalDateTime.now();

        bank.setCreatedAt(now);
        bank.setUpdatedAt(now);
        bank.setIsDeleted(false);

        try {
            var savedBank = repository.save(bank);
            return BankDto.fromDomainObject(savedBank);
        } catch (Exception e) {
            //TODO: перехватить
            throw new IllegalArgumentException("Error while saving");
        }
    }
}
