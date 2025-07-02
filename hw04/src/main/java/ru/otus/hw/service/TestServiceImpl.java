package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.exceptions.QuestionConvertException;
import ru.otus.hw.exceptions.QuestionReadException;
import ru.otus.hw.exceptions.TestServiceException;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    private final QuestionConvertor questionConvertor;

    @Override
    public TestResult executeTestFor(Student student) {
        try {
            ioService.printLine("");
            ioService.printLineLocalized("TestService.answer.the.questions");
            ioService.printLine("");

            var questions = questionDao.findAll();
            var testResult = new TestResult(student);

            for (var question : questions) {
                var isAnswerValid = askQuestionAndGetResult(question);
                testResult.applyAnswer(question, isAnswerValid);
            }
            return testResult;

        } catch (QuestionReadException e) {
            throw new TestServiceException("Error reading question", e);
        }
    }

    private boolean askQuestionAndGetResult(Question question) {
        try {
            ioService.printLine("");
            ioService.printLine(questionConvertor.convertToString(question));

            int userAnswer = ioService.readIntForRangeWithPromptLocalized(
                    1,
                    question.answers().size(),
                    "TestService.enter.the.answer",
                    "TestService.answer.out.of.range");

            return question.answers().get(userAnswer - 1).isCorrect();

        } catch (QuestionConvertException e) {
            throw new TestServiceException("Error converting question", e);

        } catch (IllegalArgumentException e) {
            throw new TestServiceException("Error during testing process", e);
        }
    }

}
