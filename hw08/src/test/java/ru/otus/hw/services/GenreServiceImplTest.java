package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import({GenreServiceImpl.class, TestDataManager.class})
class GenreServiceImplTest {

    private static final int GENRE_NUMBER = 3;

    @Autowired
    private TestDataManager testDataManager;

    @Autowired
    private GenreService genreService;

    @BeforeEach
    void refreshDb() {
        testDataManager.dropDb();
    }

    @Test
    void testFindAllShouldGetCorrectResult() {

        List<Genre> expectedGenres = new ArrayList<>();
        for (int i = 0; i < GENRE_NUMBER; i++) {
            expectedGenres.add(testDataManager.saveGenre("Genre_"+i));
        }

        List<Genre> actualGenres = genreService.findAll();

        assertThat(actualGenres).hasSize(GENRE_NUMBER)
                .containsExactlyInAnyOrderElementsOf(expectedGenres);
    }
}
