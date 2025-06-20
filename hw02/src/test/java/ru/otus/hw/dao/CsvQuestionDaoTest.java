package ru.otus.hw.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.otus.hw.dao.CsvQuestionDao.*;

public class CsvQuestionDaoTest {

    private static final String ERROR_CSV_READING = "Error reading csv file";

    @Mock
    private TestFileNameProvider fileNameProviderMock = mock(TestFileNameProvider.class);

    @DisplayName("Корректное чтение файла. Считывается один вопрос и 2 ответа")
    @Test
    public void testFindAllShouldReturnCorrectResult() {
        CsvQuestionDao csvQuestionDao = new CsvQuestionDao(fileNameProviderMock);
        when(fileNameProviderMock.getTestFileName())
                .thenReturn("questionsTestOk.csv");

        List<Question> expectedQuestions = List.of(
                new Question("Question?", List.of(
                        new Answer("Answer1", Boolean.TRUE),
                        new Answer("Answer2", Boolean.FALSE)
                ))
        );

        List<Question> questions = csvQuestionDao.findAll();

        assertThat(questions).isEqualTo(expectedQuestions);

    }

    @DisplayName("Чтение файла без первой строки с комментарием." +
            "Ожидается исключение QuestionReadException")
    @Test
    public void testFindAllWithNoFirstCommentInFileShouldGetQuestionReadException() {
        CsvQuestionDao csvQuestionDao = new CsvQuestionDao(fileNameProviderMock);

        when(fileNameProviderMock.getTestFileName())
                .thenReturn("questionsTestNoFirstLine.csv");

        assertThatExceptionOfType(QuestionReadException.class)
                .isThrownBy(csvQuestionDao::findAll)
                .withMessage(ERROR_CSV_READING);
    }

    @DisplayName("Чтение файла без вопросов. Ожидается исключение QuestionReadException")
    @Test
    public void testFindAllWithNoQuestionsShouldGeQuestionReadException() {
        CsvQuestionDao csvQuestionDao = new CsvQuestionDao(fileNameProviderMock);

        when(fileNameProviderMock.getTestFileName())
                .thenReturn("questionsTestNoQuestions.csv");

        assertThatExceptionOfType(QuestionReadException.class)
                .isThrownBy(csvQuestionDao::findAll)
                .withMessage(ERROR_CSV_READING);
    }

    @DisplayName("Чтение файла с некорректным разделителем между вопросом и ответами в первой строке." +
            "Будут считаны два вопроса, но у первого не будет ответов.")
    @Test
    public void testFindAllWithIncorrectQuestionSeparatorShouldGetNoAnswers() {
        CsvQuestionDao csvQuestionDao = new CsvQuestionDao(fileNameProviderMock);

        when(fileNameProviderMock.getTestFileName())
                .thenReturn("questionsTestIncorrectQuestionSeparator.csv");

        var questionList = csvQuestionDao.findAll();

        assertThat(questionList).hasSize(2);
        assertThat(questionList.get(0).answers()).isNull();
    }

    @DisplayName("Чтение файла, где у одного ответа пропущен флаг правильности/неправильности." +
            "Ожидается исключение QuestionReadException")
    @Test
    public void testFindAllWithoutBooleanFlagShouldGetQuestionReadException() {
        CsvQuestionDao csvQuestionDao = new CsvQuestionDao(fileNameProviderMock);

        when(fileNameProviderMock.getTestFileName())
                .thenReturn("questionsTestNoBooleanFlag.csv");

        assertThatExceptionOfType(QuestionReadException.class)
                .isThrownBy(csvQuestionDao::findAll)
                .withMessage(ERROR_CSV_READING);
    }

    @DisplayName("Чтение несуществующего файла. Ожидается исключение QuestionReadException")
    @Test
    public void testFindAllNoFileShouldGetQuestionReadException() {
        CsvQuestionDao csvQuestionDao = new CsvQuestionDao(fileNameProviderMock);

        when(fileNameProviderMock.getTestFileName())
                .thenReturn("questionsTestNoFile.csv");//несуществующий файл

        assertThatExceptionOfType(QuestionReadException.class)
                .isThrownBy(csvQuestionDao::findAll)
                .withMessage("Error reading file");
    }

    @DisplayName("Не указано имя файла для чтения. Ожидается исключение QuestionReadException")
    @Test
    public void testFindAllWithEmptyFileNameShouldGetQuestionReadException() {
        CsvQuestionDao csvQuestionDao = new CsvQuestionDao(fileNameProviderMock);

        when(fileNameProviderMock.getTestFileName())
                .thenReturn(null);//несуществующий файл

        assertThatExceptionOfType(QuestionReadException.class)
                .isThrownBy(csvQuestionDao::findAll)
                .withMessage("Error reading file");
    }
}
