package ru.otus.hw.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.exceptions.QuestionReadException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.otus.hw.dao.CsvQuestionDao.*;

public class CsvQuestionDaoTest {

    @Mock
    private TestFileNameProvider fileNameProviderMock = mock(TestFileNameProvider.class);

    @DisplayName("Корректное чтение файла: 1 вопрос и 2 ответа")
    @Test
    public void testFindAllShouldGetResultWithOneQuestionAndTwoAnswers() {
        CsvQuestionDao csvQuestionDao = new CsvQuestionDao(fileNameProviderMock);

        when(fileNameProviderMock.getTestFileName()).thenReturn("questionsTestOk.csv");

        var questionList = csvQuestionDao.findAll();

        assertThat(questionList).hasSize(1);
        assertThat(questionList.get(0)).hasFieldOrPropertyWithValue("text", "Question?");

        assertThat(questionList.get(0).answers()).hasSize(2);

        assertThat(questionList.get(0).answers().get(0)).hasFieldOrPropertyWithValue("text", "Answer1");
        assertThat(questionList.get(0).answers().get(0)).hasFieldOrPropertyWithValue("isCorrect", Boolean.TRUE);

        assertThat(questionList.get(0).answers().get(1)).hasFieldOrPropertyWithValue("text", "Answer2");
        assertThat(questionList.get(0).answers().get(1)).hasFieldOrPropertyWithValue("isCorrect", Boolean.FALSE);
    }

    @DisplayName("Ошибка парсинга для файла без комментария в первой строке")
    @Test
    public void testFindAllShouldGetExceptionNoQuestionRead() {
        CsvQuestionDao csvQuestionDao = new CsvQuestionDao(fileNameProviderMock);

        when(fileNameProviderMock.getTestFileName()).thenReturn("questionsTestNoFirstLine.csv");

        assertThatExceptionOfType(QuestionReadException.class)
                .isThrownBy(csvQuestionDao::findAll)
                .withMessage(NO_QUESTION_READ);
    }

    @DisplayName("Файл без вопросов - исключение")
    @Test
    public void testFindAllWithNoQuestionsShouldGetExceptionNoQuestionRead() {
        CsvQuestionDao csvQuestionDao = new CsvQuestionDao(fileNameProviderMock);

        when(fileNameProviderMock.getTestFileName()).thenReturn("questionsTestNoQuestions.csv");

        assertThatExceptionOfType(QuestionReadException.class)
                .isThrownBy(csvQuestionDao::findAll)
                .withMessage(NO_QUESTION_READ);
    }

    @DisplayName("Некорреткный разделитель после вопроса. Нет ответов")
    @Test
    public void testFindAllWithIncorrectQuestionSeparatorShouldGetNoAnswers() {
        CsvQuestionDao csvQuestionDao = new CsvQuestionDao(fileNameProviderMock);

        when(fileNameProviderMock.getTestFileName()).thenReturn("questionsTestIncorrectQuestionSeparator.csv");

        var questionList = csvQuestionDao.findAll();

        assertThat(questionList).hasSize(2);
        assertThat(questionList.get(0).answers()).isNull();
    }

    @DisplayName("Нет флага корректности ответа - ошибка парсинга")
    @Test
    public void testFindAllWithoutBooleanFlagShouldGetExceptionErrorCsvParsing() {
        CsvQuestionDao csvQuestionDao = new CsvQuestionDao(fileNameProviderMock);

        when(fileNameProviderMock.getTestFileName()).thenReturn("questionsTestNoBooleanFlag.csv");

        assertThatExceptionOfType(QuestionReadException.class)
                .isThrownBy(csvQuestionDao::findAll)
                .withMessage(ERROR_CSV_PARSING);
    }

    @DisplayName("Нет файла - ошибка чтения файла")
    @Test
    public void testFindAllNoFileShouldGetExceptionErrorFileReading() {
        CsvQuestionDao csvQuestionDao = new CsvQuestionDao(fileNameProviderMock);

        when(fileNameProviderMock.getTestFileName()).thenReturn("questionsTestNoFile.csv");//несуществующий файл

        assertThatExceptionOfType(QuestionReadException.class)
                .isThrownBy(csvQuestionDao::findAll)
                .withMessage(ERROR_FILE_READING);
    }
}
