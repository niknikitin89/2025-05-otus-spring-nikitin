package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.exceptions.QuestionConvertException;
import ru.otus.hw.exceptions.QuestionReadException;
import ru.otus.hw.exceptions.TestServiceException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestServiceImplTest {

    private static final String ENTER_ANSWER_TEXT = "TestService.enter.the.answer";
    private static final String ERROR_ANSWER_OUT_OF_RANGE = "TestService.answer.out.of.range";

    @Mock
    private QuestionDao questionDaoMock;

    @Mock
    private LocalizedIOService localizedIOService;

    @Mock
    private QuestionConvertor questionConvertorMock;

    @Mock
    private Student studentMock;

    private static List<Question> questionList;

    @BeforeAll
    static void init() {
        final String question1 = "Question 1?";
        final String answer11 = "Answer 11";
        final String answer12 = "Answer 12";

        final String question2 = "Question 2?";
        final String answer21 = "Answer 21";
        final String answer22 = "Answer 22";
        final String answer23 = "Answer 23";

        questionList = List.of(
                new Question(question1, List.of(
                        new Answer(answer11, true),
                        new Answer(answer12, false)
                )),
                new Question(question2, List.of(
                        new Answer(answer21, false),
                        new Answer(answer22, false),
                        new Answer(answer23, true)
                ))
        );
    }

    @DisplayName("Получен корректный результат работы сервиса")
    @Test
    void testExecuteTestShouldGetCorrectTEstResult() {

        TestResult expectedTestResult = new TestResult(studentMock);
        expectedTestResult.applyAnswer(questionList.get(0), true);
        expectedTestResult.applyAnswer(questionList.get(1), false);

        TestServiceImpl service = new TestServiceImpl(
                localizedIOService,
                questionDaoMock,
                questionConvertorMock);


        when(questionDaoMock.findAll()).thenReturn(questionList);
        when(localizedIOService.readIntForRangeWithPromptLocalized(
                1, 2, ENTER_ANSWER_TEXT, ERROR_ANSWER_OUT_OF_RANGE))
                .thenReturn(1);
        when(localizedIOService.readIntForRangeWithPromptLocalized(
                1, 3, ENTER_ANSWER_TEXT, ERROR_ANSWER_OUT_OF_RANGE))
                .thenReturn(2);

        var testResult = service.executeTestFor(studentMock);

        //Шапка
        verify(localizedIOService, times(1))
                .printLineLocalized("TestService.answer.the.questions");

        //Пустая строка
        verify(localizedIOService, times(4))
                .printLine("");

        assertThat(testResult).isEqualTo(expectedTestResult);
    }

    @DisplayName("Генерируется исключение TestServiceException, " +
            "когда DAO выбрасывает исключение QuestionReadException")
    @Test
    void testExecuteTestShouldThrowTestServiceExceptionWhenDaoThrowQuestionReadException() {

        TestServiceImpl service = new TestServiceImpl(
                localizedIOService,
                questionDaoMock,
                questionConvertorMock);

        when(questionDaoMock.findAll()).thenThrow(QuestionReadException.class);

        assertThatExceptionOfType(TestServiceException.class)
                .isThrownBy(() -> service.executeTestFor(studentMock))
                .withMessage("Error reading question");
    }

    @DisplayName("Генерируется исключение TestServiceException, " +
            "когда конвертер выбрасывает исключение QuestionConvertException")
    @Test
    void testExecuteTestShouldThrowTestServiceExceptionWhenConverterThrowQuestionConvertException() {

        TestServiceImpl service = new TestServiceImpl(
                localizedIOService,
                questionDaoMock,
                questionConvertorMock);

        when(questionDaoMock.findAll()).thenReturn(questionList);
        when(questionConvertorMock.convertToString(questionList.get(0)))
                .thenThrow(QuestionConvertException.class);

        assertThatExceptionOfType(TestServiceException.class)
                .isThrownBy(() -> service.executeTestFor(studentMock))
                .withMessage("Error converting question");
    }

    @DisplayName("Генерируется исключение TestServiceException, " +
            "когда конвертер выбрасывает исключение IllegalArgumentException")
    @Test
    void testExecuteTestShouldThrowTestServiceExceptionWhenConverterThrowIllegalArgumentException() {

        TestServiceImpl service = new TestServiceImpl(
                localizedIOService,
                questionDaoMock,
                questionConvertorMock);

        when(questionDaoMock.findAll()).thenReturn(questionList);
        when(localizedIOService.readIntForRangeWithPromptLocalized(
                1, 2, ENTER_ANSWER_TEXT, ERROR_ANSWER_OUT_OF_RANGE))
                .thenThrow(IllegalArgumentException.class);

        assertThatExceptionOfType(TestServiceException.class)
                .isThrownBy(() -> service.executeTestFor(studentMock))
                .withMessage("Error during testing process");
    }

}
