package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestServiceImplTest {

    //    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final IOService ioService = new StreamsIOService(new PrintStream(outputStreamCaptor));

    @Mock
    private QuestionDao questionDaoMock = mock(QuestionDao.class);

//    @BeforeEach
//    public void setUp() {
//        System.setOut(new PrintStream(outputStreamCaptor));
//    }

    @Test
    public void test() {
        TestServiceImpl service = new TestServiceImpl(ioService, questionDaoMock);

        List<Answer> answers = new ArrayList<>();
        answers.add(new Answer("answer1", true));
        answers.add(new Answer("answer2", false));

        Question question = new Question("Question?", answers);

        List<Question> questions = new ArrayList<>();
        questions.add(question);

        when(questionDaoMock.findAll()).thenReturn(questions);

        service.executeTest();

        String output = outputStreamCaptor.toString().trim();
        String expectedStr = "Please answer the questions below\r\n\r\n"
                + "1. Question?\r\n -answer1\r\n -answer2";

        assertThat(output).isEqualTo(expectedStr);
    }
}
