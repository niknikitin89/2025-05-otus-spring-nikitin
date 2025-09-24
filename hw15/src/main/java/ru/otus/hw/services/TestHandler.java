package ru.otus.hw.services;

import ru.otus.hw.models.Test;
import ru.otus.hw.models.TestProcess;

public interface TestHandler {

    TestProcess test(TestProcess test);
}
