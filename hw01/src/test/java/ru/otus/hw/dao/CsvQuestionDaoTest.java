package ru.otus.hw.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CsvQuestionDaoTest {

    @Mock
    private TestFileNameProvider fileNameProviderMock = mock(TestFileNameProvider.class);

    @DisplayName("Корректное чтение файла. Считывается один вопрос и 2 ответа")
    @Test
    public void testFindAllShouldReturnCorrectResult() {
        CsvQuestionDao csvQuestionDao = new CsvQuestionDao(fileNameProviderMock);
        when(fileNameProviderMock.getTestFileName()).thenReturn("questionsTest.csv");

        List<Question> expectedQuestions = List.of(
                new Question("Question?", List.of(
                        new Answer("Answer1", Boolean.TRUE),
                        new Answer("Answer2", Boolean.FALSE)
                ))
        );

        List<Question> questions = csvQuestionDao.findAll();

        assertThat(questions).isEqualTo(expectedQuestions);

    }

}
