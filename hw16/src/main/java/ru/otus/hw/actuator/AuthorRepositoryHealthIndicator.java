package ru.otus.hw.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.hw.repositories.AuthorRepository;

@Component
@RequiredArgsConstructor
public class AuthorRepositoryHealthIndicator implements HealthIndicator {

    private final AuthorRepository authorRepository;

    @Override
    public Health health() {

        var authorsNumber = authorRepository.count();

        if (authorsNumber == 0) {
            return Health.down().withDetail("message", "We lost authors!").build();
        }
        return Health.up().withDetail("message", "All fine").build();
    }
}
