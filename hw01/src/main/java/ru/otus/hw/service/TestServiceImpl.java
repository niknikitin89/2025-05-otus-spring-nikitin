package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    private final QuestionForPrintConvertor questionForPrintConvertor;

    private final PrintTestService printTestService;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");

        var questionList = questionDao.findAll();
        printTestService.printTest(questionForPrintConvertor.convertForPrint(questionList));
    }

}
