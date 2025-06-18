package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionConvertException;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class TestServiceImplTest {

    @Mock
    private QuestionDao questionDaoMock = mock(QuestionDao.class);

    @Mock
    private IOService ioServiceMock = mock(IOService.class);

    @Mock
    private QuestionConvertor questionConvertorMock = mock(QuestionConvertor.class);

    @DisplayName("Корректная работа сервиса")
    @Test
    public void testExecuteTestShouldCorrectServiceWork() {

        TestServiceImpl service = new TestServiceImpl(
                ioServiceMock, questionDaoMock,
                questionConvertorMock);

        List<Question> questionList = new ArrayList<>();
        String testText = "Test text";

        when(questionConvertorMock.convertToString(questionList))
                .thenReturn(testText);

        service.executeTest();

        //Шапка
        verify(ioServiceMock, times(1))
                .printLine("");
        verify(ioServiceMock, times(1))
                .printFormattedLine("Please answer the questions below%n");

        //Чтение вопросов из DAO
        verify(questionDaoMock, times(1))
                .findAll();

        //Конвертация
        verify(questionConvertorMock, times(1))
                .convertToString(questionList);

        //Печать
        verify(ioServiceMock, times(1))
                .printLine(testText);
    }

    @DisplayName("В методе перехватывается исключение QuestionReadException. " +
            "Выводится сообщение об ошибке на экран")
    @Test
    public void testExecuteTestWithQuestionReadExceptionShouldPrintErrorMessage() {
        TestServiceImpl service = new TestServiceImpl(
                ioServiceMock, questionDaoMock,
                questionConvertorMock);

        String errorMessage = "Error message";

        when(questionDaoMock.findAll()).thenThrow(new QuestionReadException(errorMessage));

        service.executeTest();

        verify(ioServiceMock, times(1))
                .printLine("(!)Error reading questions");
    }

    @DisplayName("В методе перехватывается исключение QuestionConvertException. " +
            "Выводится сообщение об ошибке на экран")
    @Test
    public void testExecuteTestWithQuestionConvertExceptionShouldPrintErrorMessage() {
        TestServiceImpl service = new TestServiceImpl(
                ioServiceMock, questionDaoMock,
                questionConvertorMock);

        String errorMessage = "Error message";

        when(questionConvertorMock.convertToString(anyList()))
                .thenThrow(new QuestionConvertException(errorMessage));

        service.executeTest();

        verify(ioServiceMock, times(1))
                .printLine("(!)Test conversion error");
    }
}
