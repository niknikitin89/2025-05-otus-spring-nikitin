package ru.otus.hw.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import jakarta.validation.ValidationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Configuration
public class Resilience4jConfig {

    // Circuit Breaker для сервиса авторов
    // Защищает от повторных вызовов неудачного сервиса, предотвращая каскадные сбои
    @Bean
    public CircuitBreaker authorsCircuitBreaker() {

        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                // Порог процента неудачных вызовов (50%) для переключения в состояние OPEN
                .failureRateThreshold(50)
                // Время, в течение которого Circuit Breaker остается OPEN перед переходом в HALF_OPEN
                .waitDurationInOpenState(Duration.ofSeconds(15))
                // Количество разрешенных вызовов в состоянии HALF_OPEN для тестирования восстановления сервиса
                .permittedNumberOfCallsInHalfOpenState(3)
                // Минимальное количество вызовов в окне для расчета статистики failure rate
                .minimumNumberOfCalls(5)
                // Размер скользящего окна для отслеживания вызовов (в количестве вызовов)
                .slidingWindowSize(10)
                // Типы исключений, которые считаются неудачными вызовами
                .recordExceptions(Exception.class)
                .build();

        // Создание и регистрация Circuit Breaker с именем "authors-service"
        return CircuitBreakerRegistry.of(config)
                .circuitBreaker("authors-service");
    }

    // Общий Circuit Breaker для всех остальных сервисов
    @Bean
    public CircuitBreaker defaultCircuitBreaker() {

        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold(70)
                .waitDurationInOpenState(Duration.ofSeconds(10))
                .permittedNumberOfCallsInHalfOpenState(5)
                .minimumNumberOfCalls(10)
                .slidingWindowSize(20)
                .recordExceptions(RuntimeException.class, IOException.class)
                .ignoreExceptions(IllegalArgumentException.class, ValidationException.class)
                .build();

        // Создание и регистрация Circuit Breaker с именем "default-service"
        return CircuitBreakerRegistry.of(config)
                .circuitBreaker("default-service");
    }

    // Retry для сервиса авторов
    // Автоматически повторяет неудачные вызовы с заданными интервалами
    @Bean
    public Retry authorsRetry() {

        RetryConfig config = RetryConfig.custom()
                // Максимальное количество попыток вызова (включая первоначальную)
                .maxAttempts(3)
                // Интервал ожидания между повторными попытками
                .waitDuration(Duration.ofMillis(1000))
                // Типы исключений, при которых следует выполнять повторные попытки
                .retryExceptions(Exception.class)
                .build();

        // Создание и регистрация Retry с именем "authors-service"
        return RetryRegistry.of(config)
                .retry("authors-service");
    }

    // Общий Retry для всех остальных сервисов
    @Bean
    public Retry defaultRetry() {

        RetryConfig config = RetryConfig.custom()
                .maxAttempts(2)
                .waitDuration(Duration.ofMillis(500))
                .retryExceptions(IOException.class, TimeoutException.class)
                .ignoreExceptions(IllegalArgumentException.class, ValidationException.class)
                .build();

        return RetryRegistry.of(config)
                .retry("default-service");
    }
}
