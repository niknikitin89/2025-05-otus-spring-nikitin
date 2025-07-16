package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jdbc для работы с жанрами ")
@DataJpaTest
@Import({JdbcGenreRepository.class})
class JdbcGenreRepositoryTest {

    private final int EXPECTED_NUMBER_OF_GENRES = 6;

    private final long FIRST_GENRE_ID = 1L;

    private final long SECOND_GENRE_ID = 2L;

    private final int EXPECTED_NUMBER_OF_GENRES_BY_ID_SEARCH = 2;

    @Autowired
    private JdbcGenreRepository repositoryJdbc;

    @DisplayName("должен загружать жанры по списку id")
    @Test
    void testFindAllByIdsShouldReturnCorrectGenres() {

        Set<Long> genresIdSet = new HashSet<>();
        genresIdSet.add(FIRST_GENRE_ID);
        genresIdSet.add(SECOND_GENRE_ID);

        var genres = repositoryJdbc.findAllByIds(genresIdSet);

        assertThat(genres).hasSize(EXPECTED_NUMBER_OF_GENRES_BY_ID_SEARCH)
                .allMatch(genre -> genresIdSet.contains(genre.getId()))
                .allMatch(genre -> genre.getName() != null && !genre.getName().isEmpty());
    }

    @DisplayName("должен загружать список всех жанров")
    @Test
    void testFindAllShouldReturnCorrectGenresList() {
        var genres = repositoryJdbc.findAll();

        assertThat(genres).hasSize(EXPECTED_NUMBER_OF_GENRES)
                .allMatch(genre -> genre.getName() != null && !genre.getName().isEmpty());
    }

}
