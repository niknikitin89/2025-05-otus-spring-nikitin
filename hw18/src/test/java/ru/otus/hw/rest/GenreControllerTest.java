package ru.otus.hw.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.rest.GenreController;
import ru.otus.hw.services.GenreService;

import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GenreController.class)
class GenreControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private GenreService genreService;

    @Test
    void getAllGenresShouldReturnListOfGenres() throws Exception {
        // Подготовка тестовых данных
        GenreDto genre1 = new GenreDto(1L, "Genre1");
        GenreDto genre2 = new GenreDto(2L, "Genre2");
        List<GenreDto> expectedGenres = List.of(genre1, genre2);

        // Мокирование сервиса
        when(genreService.findAll()).thenReturn(expectedGenres);

        // Выполнение запроса и проверка
        mvc.perform(get("/api/v1/genres"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedGenres)));
    }

    @Test
    void getAllGenresWhenNoGenresShouldReturnEmptyList() throws Exception {
        // Подготовка ожидаемого результата
        List<GenreDto> expectedEmptyList = List.of();

        // Мокирование сервиса
        when(genreService.findAll()).thenReturn(expectedEmptyList);

        // Выполнение запроса и проверка
        mvc.perform(get("/api/v1/genres"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedEmptyList)));
    }
}