package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question : questions) {
            var isAnswerValid = askQuestion(question); // Задать вопрос, получить ответ
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    private boolean askQuestion(Question question) {
        ioService.printLine("");
        ioService.printFormattedLine("%s%n", question.text());

        int answerNumber = 0;
        for (var answer : question.answers()) {
            answerNumber++;
            ioService.printFormattedLine("%d. %s", answerNumber, answer.text());
        }

        ioService.printFormattedLine("%n%s","Enter number of correct answer:");
        int userAnswer = ioService.readIntForRange(1, answerNumber, "Answer out of range");

        return question.answers().get(userAnswer - 1).isCorrect();
    }
}
