package ru.otus.hw.domain;

import java.util.List;

public record QuestionForPrint(String text, List<AnswerForPrint> answers) {
}
