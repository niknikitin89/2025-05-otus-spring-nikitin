package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw.services.IdMappingService;

@Configuration
public class BatchCache {

    @Bean
    public IdMappingService idMappingService() {
        return new IdMappingService();
    }

}
