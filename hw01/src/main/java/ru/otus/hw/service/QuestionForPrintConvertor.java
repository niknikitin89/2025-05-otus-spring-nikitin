package ru.otus.hw.service;

import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.QuestionForPrint;

import java.util.List;

public interface QuestionForPrintConvertor {
    public List<QuestionForPrint> convertForPrint(List<Question> questionList);
}
