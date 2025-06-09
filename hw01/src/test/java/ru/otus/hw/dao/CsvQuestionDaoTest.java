package ru.otus.hw.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Question;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CsvQuestionDaoTest {

    @Mock
    private TestFileNameProvider fileNameProviderMock = mock(TestFileNameProvider.class);

    @DisplayName("Test reading csv")
    @Test
    public void testCsvQuestionDao() {
        CsvQuestionDao csvQuestionDao = new CsvQuestionDao(fileNameProviderMock);
        when(fileNameProviderMock.getTestFileName()).thenReturn("questionsTest.csv");

        List<Question> questions = csvQuestionDao.findAll();

        assertThat(questions).hasSize(1);
        assertThat(questions.get(0)).hasFieldOrPropertyWithValue("text", "Question?");

        assertThat(questions.get(0).answers()).hasSize(2);

        assertThat(questions.get(0).answers().get(0)).hasFieldOrPropertyWithValue("text", "Answer1");
        assertThat(questions.get(0).answers().get(0)).hasFieldOrPropertyWithValue("isCorrect", Boolean.TRUE);

        assertThat(questions.get(0).answers().get(1)).hasFieldOrPropertyWithValue("text", "Answer2");
        assertThat(questions.get(0).answers().get(1)).hasFieldOrPropertyWithValue("isCorrect", Boolean.FALSE);
    }

}
