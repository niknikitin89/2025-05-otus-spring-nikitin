package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;

import java.util.List;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        // Получить вопросы из дао и вывести их с вариантами ответов
        List<Question> questionList = questionDao.findAll();
        printTest(questionList);
    }

    private void printTest(List<Question> questionList) {
        int questionIndex = 0;
        for (Question question : questionList) {
            questionIndex++;
            ioService.printLine(questionIndex + ". " + question.text());
            question.answers().forEach(answer -> ioService.printLine(" -" + answer.text()));
        }
    }
}
