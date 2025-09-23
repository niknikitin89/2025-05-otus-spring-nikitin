package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import ru.otus.hw.models.CustomerWish;
import ru.otus.hw.models.DevelopmentProcess;
import ru.otus.hw.models.Product;
import ru.otus.hw.models.Testing;
import ru.otus.hw.models.WorkStatus;
import ru.otus.hw.services.DevProcessHandler;
import ru.otus.hw.services.DevTeamService;
import ru.otus.hw.services.FilterService;
import ru.otus.hw.services.ProgrammingService;
import ru.otus.hw.services.TestHandler;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class IntegrationConfig {

    private final FilterService filterService;

    private final ProgrammingService programmingService;

    private final DevTeamService devTeamService;

    private final DevProcessHandler devProcessHandler;
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

        return MessageChannels.publishSubscribe();
    }

    @Bean
    public MessageChannelSpec<?, ?> testChannel() {

        return MessageChannels.publishSubscribe();
    }

    @Bean
    public MessageChannelSpec<?, ?> afterTestChannel() {

        return MessageChannels.publishSubscribe();
    }

    @Bean
    public MessageChannelSpec<?, ?> prefinalChannel() {

        return MessageChannels.publishSubscribe();
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
                .<CustomerWish, DevelopmentProcess>transform(devTeamService::createDevTeam)//собираем команду разработки и оформляем процесс
                .channel(devChannel())//в канал разработки
                .handle(devProcessHandler, "develop")//разрабатываем
                .channel(testChannel())//в канал тестирования
                .split(new TestingListSplitter())//делим список тестов
                .handle(testHandler, "test")
                .aggregate(aggregator -> aggregator
                        .correlationStrategy(message ->
                                message.getHeaders().get("correlationId"))
                        .releaseStrategy(group -> group.size()==3)
                        .outputProcessor(group -> {
                            List<Testing> results = group.getMessages().stream()
                                    .map(message -> (Testing) message.getPayload())
                                    .toList();

                            DevelopmentProcess original = (DevelopmentProcess)
                                    group.getOne().getHeaders().get("originalProcess");
                            original.setTestingList(results);
                            return original;
                        }))
                .<DevelopmentProcess, Product>transform(programmingService::createProduct)// конвертим хотелки в продукт
                .aggregate()
                .get();
    }
}
