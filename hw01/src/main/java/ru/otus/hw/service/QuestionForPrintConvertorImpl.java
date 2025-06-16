package ru.otus.hw.service;

import lombok.NoArgsConstructor;
import ru.otus.hw.domain.AnswerForPrint;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.QuestionForPrint;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class QuestionForPrintConvertorImpl implements QuestionForPrintConvertor {
    @Override
    public List<QuestionForPrint> convertForPrint(List<Question> questionList) {
        List<QuestionForPrint> questionForPrintList = new ArrayList<>();

        for (Question question : questionList) {
            List<AnswerForPrint> answersForPrint = new ArrayList<>();
            question.answers().forEach(
                    answer -> answersForPrint.add(
                            new AnswerForPrint(answer.text())));

            questionForPrintList.add(
                    new QuestionForPrint(question.text(), answersForPrint));
        }
        return questionForPrintList;
    }
}
