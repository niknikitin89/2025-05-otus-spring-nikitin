package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import ru.otus.hw.models.CustomerWish;
import ru.otus.hw.models.Process;
import ru.otus.hw.models.Product;
import ru.otus.hw.models.Test;
import ru.otus.hw.models.TestProcess;
import ru.otus.hw.services.DevelopmentService;
import ru.otus.hw.services.DreamTeamService;
import ru.otus.hw.services.FilterService;
import ru.otus.hw.services.ProductReliaseService;
import ru.otus.hw.services.TestHandler;

import java.util.List;

//@Configuration
@RequiredArgsConstructor
public class IntegrationConfig {

    private final FilterService filterService;

    private final ProductReliaseService productReliaseService;

    private final DreamTeamService dreamTeamService;

    private final DevelopmentService developmentService;

    private final TestHandler testHandler;

    @Bean
    public MessageChannelSpec<?, ?> wishChannel() {

        return MessageChannels.queue(10);
    }

    @Bean
    public MessageChannelSpec<?, ?> readyToWorkChannel() {

        return MessageChannels.publishSubscribe();
    }

    @Bean
    public MessageChannelSpec<?, ?> productChannel() {

        return MessageChannels.publishSubscribe();
    }

    @Bean
    public MessageChannelSpec<?, ?> devChannel() {

        return MessageChannels.direct();
    }

    @Bean
    public MessageChannelSpec<?, ?> testChannel() {

        return MessageChannels.direct();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerSpec poller() {

        return Pollers.fixedRate(100).maxMessagesPerPoll(2);
    }

    @Bean
    public IntegrationFlow itCompanyFlow() {

        return IntegrationFlow.from(wishChannel())              //получаем хотелки от заказчика
                .split()
                .<CustomerWish>filter(filterService::readyToPay)//отсеиваем хотелки, за которые платить не хотят
                .channel(readyToWorkChannel())
                .<CustomerWish, Process>transform(dreamTeamService::createDevTeam)//собираем команду
                // разработки и оформляем процесс
                .channel(devChannel())//в канал разработки
                .handle(developmentService, "develop")//разрабатываем
                .channel(testChannel())//в канал тестирования
                .split(new TestListSplitter())//делим весь процесс по перечню тестов,
                // чтобы каждый тест отработать отдельно

                .channel(MessageChannels.executor(taskExecutor()))//запускаем параллельные потоки

                .handle(testHandler, "test")// выполняем тесты
                .aggregate(aggregator -> aggregator // агрегируем тесты обратно в одно сообщение с конвертацией в процесс
                        .groupTimeout(5000) // 5 секунд таймаут на группу
                        .correlationStrategy(message ->
                                message.getHeaders().get("correlationId")) //агрегируем по correlationId,
                        // он у нас тянется еще с первого split и в каждом сообщении для теста одинаковый
                        .releaseStrategy(group -> group.size() == 3)//группа собрана, когда получили три сообщения
                        .outputProcessor(group -> {
                            List<Test> results = group.getMessages().stream()
                                    .map(message -> {
                                        var testProcess = (TestProcess) message.getPayload();
                                        return testProcess.getTest();
                                    })
                                    .toList();

                            Process original = (Process)
                                    group.getOne().getHeaders().get("originalProcess");
                            original.setTestList(results);
                            return original;
                        }))
                .<Process, Product>transform(productReliaseService::createProduct)//конвертим процесс в продукт
                .aggregate(aggregator -> aggregator
                        .groupTimeout(5000) // 5 секунд таймаут на группу
                        .expireGroupsUponCompletion(true) // Очищать группы после завершения
                        .expireGroupsUponTimeout(true)    // Очищать группы при таймауте
                        .sendPartialResultOnExpiry(true)  // Отправлять частичный результат
                )
                .channel(productChannel())
                .get();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(25);
        executor.setThreadNamePrefix("test-");
        executor.initialize();
        return executor;
    }
}
