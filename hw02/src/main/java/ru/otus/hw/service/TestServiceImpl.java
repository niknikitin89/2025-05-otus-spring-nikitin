package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.exceptions.PrintTestException;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    public static final String NO_QUESTIONS_FOUND = "No questions found";

    public static final String NO_ANSWERS_FOUND = "No answers found";

    public static final String ERROR_ANSWER_OUT_OF_RANGE = "Answer out of range";

    public static final String QUESTION_PRINT_FORMAT = "Question №%d%n%s%n";

    public static final String ANSWER_PRINT_FORMAT = "%d. %s";

    public static final String ENTER_ANSWER_TEXT = "Enter number of correct answer:";

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        try {
            ioService.printLine("");
            ioService.printFormattedLine("Please answer the questions below%n");
            var questions = questionDao.findAll();
            var testResult = new TestResult(student);

            if (questions == null || questions.isEmpty()) {
                throw new IllegalArgumentException(NO_QUESTIONS_FOUND);
            }

            int questionNumber = 0;
            for (var question : questions) {
                questionNumber++;
                var isAnswerValid = askQuestion(questionNumber, question); // Задать вопрос, получить ответ
                testResult.applyAnswer(question, isAnswerValid);
            }
            return testResult;

        } catch (IllegalArgumentException e) {
            throw new PrintTestException(e.getMessage(), e);
        }
    }

    private boolean askQuestion(int questionNumber, Question question)
            throws IllegalArgumentException {
        ioService.printLine("");
        ioService.printFormattedLine(QUESTION_PRINT_FORMAT, questionNumber, question.text());

        if (question.answers() == null || question.answers().isEmpty()) {
            throw new IllegalArgumentException(NO_ANSWERS_FOUND);
        }

        int answerNumber = 0;
        for (var answer : question.answers()) {
            answerNumber++;
            ioService.printFormattedLine(ANSWER_PRINT_FORMAT, answerNumber, answer.text());
        }

        int userAnswer = ioService.readIntForRangeWithPrompt(
                1, answerNumber, ENTER_ANSWER_TEXT, ERROR_ANSWER_OUT_OF_RANGE);
        return question.answers().get(userAnswer - 1).isCorrect();
    }
}
