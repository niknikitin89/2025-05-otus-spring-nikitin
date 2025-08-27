package ru.otus.hw.controllers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.services.GenreService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GenreController.class)
@Import(SecurityConfiguration.class)
class GenreControllerSecurityTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private GenreService genreService;

    private List<GenreDto> expectedGenres;

    @BeforeEach
    void setUp() {
        GenreDto genre1 = new GenreDto(1L, "Genre1");
        GenreDto genre2 = new GenreDto(2L, "Genre2");
        expectedGenres = List.of(genre1, genre2);
    }

    @Test
    @WithMockUser
    void getAllGenresWithAuthorizationShouldGetAccess() throws Exception {
        when(genreService.findAll()).thenReturn(expectedGenres);

        mvc.perform(get("/all_genres"))
                .andExpect(status().isOk())
                .andExpect(view().name("allGenresPage"))
                .andExpect(model().attributeExists("genres"))
                .andExpect(model().attribute("genres", expectedGenres));
    }

    @Test
    void getAllGenresWithoutAuthorizationShouldRedirectToLogin() throws Exception {
        when(genreService.findAll()).thenReturn(expectedGenres);

        mvc.perform(get("/all_genres"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }
}
