package ru.otus.hw.service;

import lombok.NoArgsConstructor;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.PrintTestException;

import java.util.List;

@NoArgsConstructor
public class QuestionForPrintConvertorImpl implements QuestionForPrintConvertor {
    @Override
    public String convertForPrint(List<Question> questionList) throws PrintTestException {
        if (questionList == null || questionList.isEmpty()) {
            throw new PrintTestException("No questions found");
        }

        StringBuilder sb = new StringBuilder();
        for (int questionIndex = 0; questionIndex < questionList.size(); questionIndex++) {
            var question = questionList.get(questionIndex);
            printQuestion(sb, questionIndex, question);
            printAnswers(sb, question.answers());
        }

        return sb.toString();
    }


    private void printQuestion(StringBuilder sb, int questionIndex, Question question) {
        sb.append(String.format("%d. %s%n", questionIndex + 1, question.text()));
    }

    private void printAnswers(StringBuilder sb, List<Answer> answers) {
        if (answers == null || answers.isEmpty()) {
            throw new PrintTestException("No answers found");
        }

        char answerPointer = 'a';

        for (Answer answer : answers) {
            sb.append(String.format(" %c - %s%n", answerPointer, answer.text()));
            answerPointer = (char) (answerPointer + 1);
        }
    }
}
