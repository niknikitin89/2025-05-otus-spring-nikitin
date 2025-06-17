package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.exceptions.PrintTestException;
import ru.otus.hw.exceptions.QuestionReadException;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    private final QuestionForPrintConvertor questionForPrintConvertor;

    @Override
    public void executeTest() {
        try {
            ioService.printLine("");
            ioService.printFormattedLine("Please answer the questions below%n");

            var questionList = questionDao.findAll();
            ioService.printLine(questionForPrintConvertor.convertForPrint(questionList));
        } catch (QuestionReadException e) {
            ioService.printLine("(!)Error reading file: " + e.getMessage());
            e.printStackTrace();
        } catch (PrintTestException e) {
            ioService.printLine("(!)Text printing error: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
