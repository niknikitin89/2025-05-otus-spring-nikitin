package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import({AuthorServiceImpl.class, TestDataManager.class})
class AuthorServiceImplTest {

    private static final int AUTHOR_NUMBER = 3;

    @Autowired
    private TestDataManager testDataManager;

    @Autowired
    private AuthorService authorService;

    @BeforeEach
    void refreshDb() {
        testDataManager.dropDb();
    }

    @Test
    void testFindAllShouldGetCorrectResult() {

        List<Author> expectedAuthors = new ArrayList<>();
        for (int i = 0; i < AUTHOR_NUMBER; i++) {
            expectedAuthors.add(testDataManager.saveAuthor("Author_"+i));
        }

        List<Author> actualAuthors = authorService.findAll();

        assertThat(actualAuthors).hasSize(AUTHOR_NUMBER)
                .containsExactlyInAnyOrderElementsOf(expectedAuthors);
    }

}
