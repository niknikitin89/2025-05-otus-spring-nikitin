package ru.otus.hw.service;

import ru.otus.hw.domain.QuestionForPrint;
import ru.otus.hw.exceptions.PrintTestException;

import java.util.List;

public interface PrintTestService {
    public void printTest(List<QuestionForPrint> questionList) throws PrintTestException;
}
