package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenreController.class)
@Import(SecurityConfiguration.class)
class GenreControllerSecurityTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private GenreService genreService;

    @BeforeEach
    void setUp() {
        GenreDto genre1 = new GenreDto(1L, "Genre1");
        GenreDto genre2 = new GenreDto(2L, "Genre2");
        List<GenreDto> expectedGenres = List.of(genre1, genre2);

        when(genreService.findAll()).thenReturn(expectedGenres);
    }

    @ParameterizedTest
    @MethodSource("getTestData")
    void shouldReturnExpectedStatus(
            String method, String url,
            String username, String role,
            int status, String redirect) throws Exception {

        var request = method2RequestBuilder(method, url);
        if (username != null) {
            request.with(user(username).roles(role));
        }

        ResultActions result = mvc.perform(request);

        if (status > 0) {
            result.andExpect(status().is(status));
        }
        if (redirect != null) {
            result.andExpect(redirectedUrl(redirect));
        }
    }


    private MockHttpServletRequestBuilder method2RequestBuilder(String method, String url) {
        Map<String, Function<String, MockHttpServletRequestBuilder>> methodMap =
                Map.of("get", MockMvcRequestBuilders::get);

        return methodMap.get(method).apply(url);
    }

    public static Stream<Arguments> getTestData() {
        return Stream.of(
                Arguments.of("get", "/all_genres", "username", "USER", 200, null),
                Arguments.of("get", "/all_genres", null, null, 302, "http://localhost/login")
        );
    }
}
