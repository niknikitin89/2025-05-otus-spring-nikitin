package ru.otus.hw.services;

import org.springframework.stereotype.Service;
import ru.otus.hw.models.Test;
import ru.otus.hw.models.WorkStatus;

@Service
public class TestServiceImpl implements TestService {

    @Override
    public Test test(Test test) {
        test.setStatus(WorkStatus.DONE);
        return test;
    }
}
