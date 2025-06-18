package ru.otus.hw.service;

import ru.otus.hw.domain.Question;

import java.util.List;

public interface QuestionConvertor {
    public String convertToString(List<Question> questionList);
}
