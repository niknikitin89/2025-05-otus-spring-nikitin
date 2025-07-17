package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jdbc для работы с авторами ")
@DataJpaTest
@Import({JpaAuthorRepository.class})
class JpaAuthorRepositoryTest {

    private static final int EXPECTED_NUMBER_OF_AUTHORS = 3;

    @Autowired
    private AuthorRepository repositoryJdbc;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать всех авторов")
    @Test
    void testFindAllShouldReturnCorrectAuthorsList() {

        var authors = repositoryJdbc.findAll();

        assertThat(authors).hasSize(EXPECTED_NUMBER_OF_AUTHORS)
                .allMatch(author -> author.getFullName() != null && !author.getFullName().isEmpty());
    }

    @DisplayName("должен загружать автора по Id")
    @Test
    void testFindByIdShouldReturnCorrectAuthor() {

        Author expectedAuthor = new Author(0,"AuthorFullname");
        expectedAuthor = em.persist(expectedAuthor);

        var author = repositoryJdbc.findById(expectedAuthor.getId());

        assertThat(author).isPresent().get()
                .isEqualTo(expectedAuthor);
    }

}
