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
import ru.otus.hw.models.Product;
import ru.otus.hw.services.FilterService;
import ru.otus.hw.services.ProgrammingService;

@Configuration
@RequiredArgsConstructor
public class IntegrationConfig {

    private final FilterService filterService;

    private final ProgrammingService programmingService;

    @Bean
    public MessageChannelSpec<?, ?> specificationChannel() {

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

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerSpec poller() {

        return Pollers.fixedRate(100).maxMessagesPerPoll(2);
    }

    @Bean
    public IntegrationFlow itCompanyFlow() {

        return IntegrationFlow.from(specificationChannel())
                .<CustomerWish>filter(filterService::readyToPay)
                .channel(readyToWorkChannel())
                .<CustomerWish, Product>transform(programmingService::createProduct)
                .channel(productChannel())
                .get();
    }
}
