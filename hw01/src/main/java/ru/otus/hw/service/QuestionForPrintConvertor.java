package ru.otus.hw.service;

import ru.otus.hw.domain.Question;

import java.util.List;

public interface QuestionForPrintConvertor {
    public String convertForPrint(List<Question> questionList);
}
