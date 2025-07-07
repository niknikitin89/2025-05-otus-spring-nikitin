package ru.otus.hw.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;


@SpringBootTest(classes = {CsvQuestionDao.class})
class CsvQuestionDaoTest {

    private static final String ERROR_CSV_READING = "Error reading csv file";

    @MockitoBean
    private TestFileNameProvider fileNameProviderMock;

    @Autowired
    private CsvQuestionDao csvQuestionDao;

    @DisplayName("Корректное чтение файла. Считывается один вопрос и 2 ответа")
    @Test
    void testFindAllShouldReturnCorrectResult() {

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

    @DisplayName("Чтение файлов с нарушением шаблона данных. Ожидается исключение QuestionReadException")
    @ParameterizedTest(name = "Файл - {0}")
    @ValueSource(strings = {"questionsTestNoFirstLine.csv", "questionsTestNoQuestions.csv",
            "questionsTestNoBooleanFlag.csv"})
    void testFindAllWithIncorrectFileDataShouldGetQuestionReadException(String filename) {

        when(fileNameProviderMock.getTestFileName())
                .thenReturn(filename);

        assertThatExceptionOfType(QuestionReadException.class)
                .isThrownBy(csvQuestionDao::findAll)
                .withMessage(ERROR_CSV_READING);
    }


    @DisplayName("Чтение файла с некорректным разделителем между вопросом и ответами в первой строке." +
            "Будут считаны два вопроса, но у первого не будет ответов.")
    @Test
    void testFindAllWithIncorrectQuestionSeparatorShouldGetNoAnswers() {

        when(fileNameProviderMock.getTestFileName())
                .thenReturn("questionsTestIncorrectQuestionSeparator.csv");

        var questionList = csvQuestionDao.findAll();

        assertThat(questionList).hasSize(2);
        assertThat(questionList.get(0).answers()).isNull();
    }

    @DisplayName("Чтение несуществующего файла. Ожидается исключение QuestionReadException")
    @Test
    void testFindAllNoFileShouldGetQuestionReadException() {
        when(fileNameProviderMock.getTestFileName())
                .thenReturn("questionsTestNoFile.csv");//несуществующий файл

        assertThatExceptionOfType(QuestionReadException.class)
                .isThrownBy(csvQuestionDao::findAll)
                .withMessage("Error reading file");
    }

    @DisplayName("Не указано имя файла для чтения. Ожидается исключение QuestionReadException")
    @Test
    void testFindAllWithEmptyFileNameShouldGetQuestionReadException() {
        when(fileNameProviderMock.getTestFileName())
                .thenReturn(null);//несуществующий файл

        assertThatExceptionOfType(QuestionReadException.class)
                .isThrownBy(csvQuestionDao::findAll)
                .withMessage("Error reading file");
    }
}
