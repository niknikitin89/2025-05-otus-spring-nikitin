package ru.otus.hw.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AppPropertiesTest {

    private AppProperties appProperties;

    @DisplayName("Корректное чтение имени файла")
    @Test
    public void testGetTestFilenameShouldGetCorrectFilename() {
        String filePath = "path.txt";
        appProperties = new AppProperties(filePath);
        assertThat(appProperties.getTestFileName()).isEqualTo(filePath);
    }
}
