package ru.otus.hw.config;

import org.springframework.integration.splitter.AbstractMessageSplitter;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import ru.otus.hw.models.Process;
import ru.otus.hw.models.Test;
import ru.otus.hw.models.TestProcess;

import java.util.List;

public class TestListSplitter extends AbstractMessageSplitter {

    @Override
    protected List<Message<TestProcess>> splitMessage(Message<?> message) {
        //Пересоберем сообщение. Нужно вытащить список работ по тестированию
        Process process = (Process) message.getPayload();
        List<Test> testList = process.getTestList();
        var productName = process.getProductName();

        return testList.stream()
                .map(test -> MessageBuilder.withPayload(new TestProcess(productName, test))
                        .copyHeaders(message.getHeaders())
                        .setHeader("originalProcess", process)//суем оригинал сообщения в заголовок,
                        // чтобюы потом все вместе собрать
                        .setHeader("splitType", "TESTING")//пометим, что это часть testingList
                        .build())
                .toList();
    }
}
