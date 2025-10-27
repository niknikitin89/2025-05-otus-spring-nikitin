package ru.otus.hw.repositories;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Bank;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BankRepositoryTest {

    private static final int EXPECTED_NUMBER_OF_BANKS = 5;

    private static final int EXPECTED_NUMBER_OF_NOT_DELETED_BANKS = 3;

    @Autowired
    private BankRepository repository;

    @Autowired
    private TestEntityManager em;

    @Test
    void findAllShouldGetAllBanks() {

        var banks = repository.findAll();
        assertThat(banks).hasSize(EXPECTED_NUMBER_OF_BANKS)
                .allMatch(b -> b.getName() != null && !b.getName().isEmpty());
    }

//    void findAllByOrderByName() {
//
//    }
//
    @Test
    void findByIsDeletedFalse() {
        var banks = repository.findByIsDeletedFalse();
        assertThat(banks).hasSize(EXPECTED_NUMBER_OF_NOT_DELETED_BANKS);
    }

    @Test
    void findByIdShouldReturnCorrectBank() {

        Bank expectedBank = new Bank();
        expectedBank.setName("New Bank");
        expectedBank = em.persist(expectedBank);

        var bank = repository.findById(expectedBank.getId());

        assertThat(bank).isPresent().get()
                .isEqualTo(expectedBank);

    }

    @Test
    void findByIdAndIsDeletedFalseShouldNotReturnDeletedBank() {
        Bank deletedBank = new Bank();
        deletedBank.setName("Deleted Bank");
        deletedBank = em.persist(deletedBank);

        var bank = repository.findByIdAndIsDeletedFalse(deletedBank.getId());

        assertThat(bank).isPresent().get().isEqualTo(deletedBank);

        deletedBank.setIsDeleted(true);
        deletedBank = em.persist(deletedBank);

        bank = repository.findByIdAndIsDeletedFalse(deletedBank.getId());

        assertThat(bank).isEmpty();

    }
}