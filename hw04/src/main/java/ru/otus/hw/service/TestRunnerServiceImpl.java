package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.QuestionConvertException;
import ru.otus.hw.exceptions.QuestionReadException;
import ru.otus.hw.exceptions.TestServiceException;

@Service
@RequiredArgsConstructor
public class TestRunnerServiceImpl implements TestRunnerService {

    private final TestService testService;

    private final StudentService studentService;

    private final ResultService resultService;

    private final LocalizedIOService localizedIOService;

    @Override
    public void run() {
        try {
        var student = studentService.determineCurrentStudent();
        var testResult = testService.executeTestFor(student);
        resultService.showResult(testResult);
        } catch (TestServiceException e) {
            processExceptions(e);
        }
    }

    private void processExceptions(TestServiceException e) {
        var exc = e.getCause();
        if (exc instanceof QuestionConvertException ||
                exc instanceof QuestionReadException) {
            localizedIOService.printLineLocalized("TestRunnerService.error.reading.questions");
        } else {
            localizedIOService.printLineLocalized("TestRunnerService.error.during.testing.process");
        }
    }
}
