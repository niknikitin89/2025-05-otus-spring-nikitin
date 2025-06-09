package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private static final String NO_QUESTIONS_FOUND = "No questions found";

    private static final String NO_ANSWERS_FOUND = "No answers found";

    private static final char FIRST_SYMBOL = 'a';

    private static final String QUESTION_PRINT_FORMAT = "%d. %s";

    private static final String ANSWER_PRINT_FORMAT = " %c - %s";

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        // Получить вопросы из дао и вывести их с вариантами ответов
        printTest(questionDao.findAll());
    }

    private void printTest(List<Question> questionList) {
        if (questionList == null || questionList.isEmpty()) {
            throw new IllegalArgumentException(NO_QUESTIONS_FOUND);
        }

        int questionIndex = 0;
        for (Question question : questionList) {
            questionIndex++;
            ioService.printFormattedLine(QUESTION_PRINT_FORMAT, questionIndex, question.text());
            printAnswers(question.answers());
        }
    }

    private void printAnswers(List<Answer> answers) {
        if (answers == null || answers.isEmpty()) {
            throw new IllegalArgumentException(NO_ANSWERS_FOUND);
        }

        char answerPointer = FIRST_SYMBOL;

        for (Answer answer : answers) {
            ioService.printFormattedLine(ANSWER_PRINT_FORMAT, answerPointer, answer.text());
            answerPointer = (char) (answerPointer + 1);
        }
    }
}
