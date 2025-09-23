package ru.otus.hw.services;

import org.springframework.stereotype.Service;
import ru.otus.hw.models.Testing;
import ru.otus.hw.models.WorkStatus;

@Service
public class TestHandlerImpl implements TestHandler {

    @Override
    public Testing test(Testing testing) {

        switch (testing.getType()){
            case UNIT -> {
                System.out.println("======================>>>>> Unit Test Done");
                testing.setStatus(WorkStatus.DONE);//место для вызова сервиса юнит-тестирования
            }
            case INTEGRATION -> {
                System.out.println("======================>>>>> Integration Test Done");
                testing.setStatus(WorkStatus.DONE);//место для вызова сервиса интеграционного тестирования
            }
            case PERFORMANCE -> {
                System.out.println("======================>>>>> Performance Test Done");
                testing.setStatus(WorkStatus.DONE);//место для вызова сервиса тестирования производительности
            }
            default -> testing.setStatus(WorkStatus.WAITING);
        }

        return testing;
    }
}
