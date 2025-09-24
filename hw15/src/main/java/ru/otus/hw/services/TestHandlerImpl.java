package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Test;
import ru.otus.hw.models.TestProcess;
import ru.otus.hw.models.WorkStatus;

@Service
@Slf4j
@RequiredArgsConstructor
public class TestHandlerImpl implements TestHandler {

    private final TestService testService;

    @Override
    public TestProcess test(TestProcess testProcess) {

        switch (testProcess.getTest().getType()){
            case UNIT -> {
                log.info("======================>>>>> \"%s\" - Unit Test Done"
                        .formatted(testProcess.getProductName()));
                var test = testService.test(testProcess.getTest());
                testProcess.setTest(test);
            }
            case INTEGRATION -> {
                log.info("======================>>>>> \"%s\" - Integration Test Done"
                        .formatted(testProcess.getProductName()));
                testProcess.getTest().setStatus(WorkStatus.DONE);//место для вызова сервиса интеграционного тестирования
            }
            case PERFORMANCE -> {
                log.info("======================>>>>> \"%s\" - Performance Test Done"
                        .formatted(testProcess.getProductName()));
                testProcess.getTest().setStatus(WorkStatus.DONE);//место для вызова сервиса тестирования производительности
            }
            default -> testProcess.getTest().setStatus(WorkStatus.WAITING);
        }

        return testProcess;
    }
}
