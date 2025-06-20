package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.exceptions.QuestionConvertException;
import ru.otus.hw.exceptions.QuestionReadException;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    private final QuestionConvertor questionConvertor;

    @Override
    public void executeTest() {
        try {
            ioService.printLine("");
            ioService.printFormattedLine("Please answer the questions below%n");

            var questionList = questionDao.findAll();
            ioService.printLine(questionConvertor.convertToString(questionList));
        } catch (QuestionReadException e) {
            ioService.printLine("(!)Error reading questions");
        } catch (QuestionConvertException e) {
            ioService.printLine("(!)Test conversion error");
        }
    }

}
