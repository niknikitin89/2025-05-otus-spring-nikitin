package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.QuestionForPrint;

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

    @Mock
    private PrintTestService printTestServiceMock = mock(PrintTestService.class);

    @DisplayName("Корректная работа сервиса")
    @Test
    public void testExecuteTestShouldCorrectServiceWork() {

        TestServiceImpl service = new TestServiceImpl(
                ioServiceMock, questionDaoMock,
                questionForPrintConvertorMock, printTestServiceMock);

        List<Question> questionList = new ArrayList<>();
        List<QuestionForPrint> convertedQuestionList = new ArrayList<>();

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
        //Печать теста
        verify(printTestServiceMock, times(1))
                .printTest(convertedQuestionList);

    }
}
