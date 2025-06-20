package ru.otus.hw.service;

import lombok.NoArgsConstructor;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionConvertException;

import java.util.List;

@NoArgsConstructor
public class QuestionConvertorImpl implements QuestionConvertor {
    @Override
    public String convertToString(List<Question> questionList) throws QuestionConvertException {
        if (questionList == null || questionList.isEmpty()) {
            throw new QuestionConvertException("No questions found");
        }

        StringBuilder sb = new StringBuilder();
        for (int questionIndex = 0; questionIndex < questionList.size(); questionIndex++) {
            var question = questionList.get(questionIndex);
            convertQuestion(sb, questionIndex, question);
            convertAnswers(sb, question.answers());
        }

        return sb.toString();
    }


    private void convertQuestion(StringBuilder sb, int questionIndex, Question question) {
        sb.append(String.format("%d. %s%n", questionIndex + 1, question.text()));
    }

    private void convertAnswers(StringBuilder sb, List<Answer> answers) {
        if (answers == null || answers.isEmpty()) {
            throw new QuestionConvertException("No answers found");
        }

        char answerPointer = 'a';

        for (Answer answer : answers) {
            sb.append(String.format(" %c - %s%n", answerPointer, answer.text()));
            answerPointer = (char) (answerPointer + 1);
        }
    }
}
