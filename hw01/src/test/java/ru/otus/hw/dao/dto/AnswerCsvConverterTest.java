package ru.otus.hw.dao.dto;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.otus.hw.domain.Answer;

import static org.assertj.core.api.Assertions.assertThat;

public class AnswerCsvConverterTest {

    private final AnswerCsvConverter converter = new AnswerCsvConverter();

    @ParameterizedTest(name = "Process answer \"{0}\" and correction \"{1}\"")
    @CsvSource({"Answer,true", "TEXT,false"})
    public void convert(String answerText, boolean correction) {
        Answer answer = new Answer(answerText, correction);
        String textLine = answerText + "%" + correction;
        Object result = converter.convertToRead(textLine);
        assertThat((Answer) result).isEqualTo(answer);
    }
}
