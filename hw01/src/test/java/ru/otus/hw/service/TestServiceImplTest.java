package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.*;
import static ru.otus.hw.service.TestServiceImpl.*;

public class TestServiceImplTest {

    @Mock
    private QuestionDao questionDaoMock = mock(QuestionDao.class);

    @Mock
    private IOService ioServiceMock = mock(IOService.class);


    @DisplayName("Корректный вывод теста")
    @Test
    public void testExecuteTestShouldPrintCorrectTestText() {
        final String question1 = "Question 1?";
        final String answer11 = "Answer 11";
        final String answer12 = "Answer 12";

        final String question2 = "Question 2?";
        final String answer21 = "Answer 21";
        final String answer22 = "Answer 22";
        final String answer23 = "Answer 23";


        TestServiceImpl service = new TestServiceImpl(ioServiceMock, questionDaoMock);

        List<Question> questionList = List.of(
                new Question(question1, List.of(
                        new Answer(answer11,true),
                        new Answer(answer12,false)
                )),
                new Question(question2, List.of(
                        new Answer(answer21,false),
                        new Answer(answer22,false),
                        new Answer(answer23,true)
                ))
        );

        when(questionDaoMock.findAll()).thenReturn(questionList);

        service.executeTest();

        //Шапка
        verify(ioServiceMock, times(1))
                .printFormattedLine("Please answer the questions below%n");

        //Вопрос 1
        verify(ioServiceMock, times(1))
                .printFormattedLine(QUESTION_PRINT_FORMAT, 1, question1);
        verify(ioServiceMock, times(1))
                .printFormattedLine(ANSWER_PRINT_FORMAT, 'a', answer11);
        verify(ioServiceMock, times(1))
                .printFormattedLine(ANSWER_PRINT_FORMAT, 'b', answer12);

        //Вопрос2
        verify(ioServiceMock, times(1))
                .printFormattedLine(QUESTION_PRINT_FORMAT, 2, question2);
        verify(ioServiceMock, times(1))
                .printFormattedLine(ANSWER_PRINT_FORMAT, 'a', answer21);
        verify(ioServiceMock, times(1))
                .printFormattedLine(ANSWER_PRINT_FORMAT, 'b', answer22);
        verify(ioServiceMock, times(1))
                .printFormattedLine(ANSWER_PRINT_FORMAT, 'c', answer23);

        //Пустая строка
        verify(ioServiceMock, times(1))
                .printLine("");
    }

    @DisplayName("Исключение, когда в списке вопросов NULL")
    @Test
    public void testExecuteTestShouldThrowExceptionWhenQuestionListIsNull() {

        TestServiceImpl service = new TestServiceImpl(ioServiceMock, questionDaoMock);
        when(questionDaoMock.findAll()).thenReturn(null);

        assertThatIllegalArgumentException()
                .isThrownBy(service::executeTest)
                .withMessage(NO_QUESTIONS_FOUND);
    }

    @DisplayName("Исключение, когда в список вопросов пуст")
    @Test
    public void testExecuteTestShouldThrowExceptionWhenQuestionListIsEmpty() {
        List<Question> questionEmptyList = new ArrayList<>();

        TestServiceImpl service = new TestServiceImpl(ioServiceMock, questionDaoMock);
        when(questionDaoMock.findAll()).thenReturn(questionEmptyList);

        assertThatIllegalArgumentException()
                .isThrownBy(service::executeTest)
                .withMessage(NO_QUESTIONS_FOUND);
    }

    @DisplayName("Исключение, когда в списке ответов NULL")
    @Test
    public void testExecuteTestShouldThrowExceptionWhenAnswerListIsNull() {
        List<Question> questions = List.of(new Question("Question?", null));

        TestServiceImpl service = new TestServiceImpl(ioServiceMock, questionDaoMock);
        when(questionDaoMock.findAll()).thenReturn(questions);

        assertThatIllegalArgumentException()
                .isThrownBy(service::executeTest)
                .withMessage(NO_ANSWERS_FOUND);
    }

    @DisplayName("Исключение, когда список ответов пуст")
    @Test
    public void testExecuteTestShouldThrowExceptionWhenAnswerListIsEmpty() {
        List<Question> questionsWithEmptyAnswerList = List.of(new Question("Question?", new ArrayList<Answer>()));

        TestServiceImpl service = new TestServiceImpl(ioServiceMock, questionDaoMock);
        when(questionDaoMock.findAll()).thenReturn(questionsWithEmptyAnswerList);

        assertThatIllegalArgumentException()
                .isThrownBy(service::executeTest)
                .withMessage(NO_ANSWERS_FOUND);
    }


}
