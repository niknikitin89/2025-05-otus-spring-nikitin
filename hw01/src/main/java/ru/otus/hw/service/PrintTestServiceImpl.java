package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.domain.AnswerForPrint;
import ru.otus.hw.domain.QuestionForPrint;
import ru.otus.hw.exceptions.PrintTestException;

import java.util.List;

@RequiredArgsConstructor
public class PrintTestServiceImpl implements PrintTestService {
    private static final String NO_QUESTIONS_FOUND = "No questions found";

    private static final String NO_ANSWERS_FOUND = "No answers found";

    private static final char FIRST_SYMBOL = 'a';

    private static final String QUESTION_PRINT_FORMAT = "%d. %s";

    private static final String ANSWER_PRINT_FORMAT = " %c - %s";

    private final IOService ioService;

    @Override
    public void printTest(List<QuestionForPrint> questionList) throws PrintTestException {
        if (questionList == null || questionList.isEmpty()) {
            throw new PrintTestException(NO_QUESTIONS_FOUND);
        }
        for (int questionIndex = 0; questionIndex < questionList.size(); questionIndex++) {
            var question = questionList.get(questionIndex);
            printQuestion(questionIndex, question);
            printAnswers(question.answers());
        }
    }

    private void printQuestion(int questionIndex, QuestionForPrint question) {
        ioService.printFormattedLine(QUESTION_PRINT_FORMAT, questionIndex + 1, question.text());
    }

    private void printAnswers(List<AnswerForPrint> answers) throws PrintTestException {
        if (answers == null || answers.isEmpty()) {
            throw new PrintTestException(NO_ANSWERS_FOUND);
        }

        char answerPointer = FIRST_SYMBOL;

        for (AnswerForPrint answer : answers) {
            ioService.printFormattedLine(ANSWER_PRINT_FORMAT, answerPointer, answer.text());
            answerPointer = (char) (answerPointer + 1);
        }
    }
}
