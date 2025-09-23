package ru.otus.hw.config;

import org.springframework.integration.splitter.AbstractMessageSplitter;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import ru.otus.hw.models.DevelopmentProcess;
import ru.otus.hw.models.Testing;

import java.util.List;

public class TestingListSplitter extends AbstractMessageSplitter {

    @Override
    protected List<Message<Testing>> splitMessage(Message<?> message) {
        //Пересоберем сообщение. Нужно вытащить список работ по тестированию
        DevelopmentProcess process = (DevelopmentProcess) message.getPayload();
        List<Testing> testingList = process.getTestingList();

        return testingList.stream()
                .map(testing -> MessageBuilder.withPayload(testing)
                        .copyHeaders(message.getHeaders())
                        .setHeader("originalProcess", process)//суем оригинал сообщения в заголовок,
                        // чтобюы потом все вместе собрать
                        .setHeader("splitType", "TESTING")//пометим, что это часть testingList
                        .build())
                .toList();
    }
}
