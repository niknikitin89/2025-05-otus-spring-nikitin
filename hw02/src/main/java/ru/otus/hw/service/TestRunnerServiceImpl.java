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

    private final IOService ioService;

    @Override
    public void run() {
        try {
            var student = studentService.determineCurrentStudent();
            var testResult = testService.executeTestFor(student);
            resultService.showResult(testResult);

        } catch (TestServiceException e) {
            var exc = e.getCause();
            if (exc instanceof QuestionConvertException) {
                ioService.printLine("(!)Test conversion error");
            } else if (exc instanceof QuestionReadException) {
                ioService.printLine("(!)Error reading questions");
            } else {
                ioService.printLine("(!)Error during testing process");
            }
        }
    }
}
