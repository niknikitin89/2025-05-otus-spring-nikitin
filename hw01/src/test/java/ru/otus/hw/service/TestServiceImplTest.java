package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.PrintTestException;
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
    private QuestionForPrintConvertor questionForPrintConvertorMock = mock(QuestionForPrintConvertor.class);

    @DisplayName("Корректная работа сервиса")
    @Test
    public void testExecuteTestShouldCorrectServiceWork() {

        TestServiceImpl service = new TestServiceImpl(
                ioServiceMock, questionDaoMock,
                questionForPrintConvertorMock);

        List<Question> questionList = new ArrayList<>();
        String testText = "Test text";

        when(questionForPrintConvertorMock.convertForPrint(questionList))
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
        verify(questionForPrintConvertorMock, times(1))
                .convertForPrint(questionList);

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
                questionForPrintConvertorMock);

        String errorMessage = "Error message";

        when(questionDaoMock.findAll()).thenThrow(new QuestionReadException(errorMessage));

        service.executeTest();

        verify(ioServiceMock, times(1))
                .printLine("(!)Error reading file: " + errorMessage);
    }

    @DisplayName("В методе перехватывается исключение PrintTestException. " +
            "Выводится сообщение об ошибке на экран")
    @Test
    public void testExecuteTestWithPrintTestExceptionShouldPrintErrorMessage() {
        TestServiceImpl service = new TestServiceImpl(
                ioServiceMock, questionDaoMock,
                questionForPrintConvertorMock);

        String errorMessage = "Error message";

        when(questionForPrintConvertorMock.convertForPrint(anyList()))
                .thenThrow(new PrintTestException(errorMessage));

        service.executeTest();

        verify(ioServiceMock, times(1))
                .printLine("(!)Text printing error: " + errorMessage);
    }
}
