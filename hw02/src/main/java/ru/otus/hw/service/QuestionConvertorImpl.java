package ru.otus.hw.service;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionConvertException;

import java.util.List;

@Service
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

    @Override
    public String convertToString(Question question) throws QuestionConvertException {
        if (question == null) {
            throw new QuestionConvertException("No question found");
        }

        StringBuilder sb = new StringBuilder();
        convertQuestion(sb, question);
        convertAnswers(sb, question.answers());

        return sb.toString();
    }

    private void convertQuestion(StringBuilder sb, int questionIndex, Question question) {
        sb.append(String.format("%d. %s%n", questionIndex + 1, question.text()));
    }

    private void convertQuestion(StringBuilder sb, Question question) {
        sb.append(String.format("Question.%n %s%n", question.text()));
    }

    private void convertAnswers(StringBuilder sb, List<Answer> answers) {
        if (answers == null || answers.isEmpty()) {
            throw new QuestionConvertException("No answers found");
        }

        for (int i = 0; i < answers.size(); i++) {
            Answer answer = answers.get(i);
            sb.append(String.format(" %d - %s%n", i + 1, answer.text()));
        }
    }
}
