package ru.otus.hw.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AppPropertiesTest {

    private AppProperties appProperties;

    @DisplayName("Returning value")
    @Test
    public void testAppProperties() {
        String filePath = "path.txt";
        appProperties = new AppProperties(filePath);
        assertThat(appProperties.getTestFileName()).isEqualTo(filePath);
    }
}
