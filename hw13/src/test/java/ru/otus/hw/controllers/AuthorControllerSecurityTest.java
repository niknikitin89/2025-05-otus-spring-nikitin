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
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.services.AuthorService;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorController.class)
@Import(SecurityConfiguration.class)
class AuthorControllerSecurityTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        AuthorDto author1 = new AuthorDto(1L, "Author1");
        AuthorDto author2 = new AuthorDto(2L, "Author2");
        List<AuthorDto> expectedAuthors = List.of(author1, author2);

        when(authorService.findAll()).thenReturn(expectedAuthors);
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
                Arguments.of("get", "/all_authors", "username", "USER", 403, null),
                Arguments.of("get", "/all_authors", "username", "ADMIN", 200, null),
                Arguments.of("get", "/all_authors", null, null, 302, "http://localhost/login")
        );
    }
}
