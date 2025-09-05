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
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentaryDto;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentaryService;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentaryController.class)
@Import(SecurityConfiguration.class)
class CommentaryControllerSecurityTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private CommentaryService commentaryService;

    @MockBean
    private BookService bookService;

    @BeforeEach
    void setUp() {
        BookDto bookDto = new BookDto(1L, "Test Book", null, null);
        CommentaryDto commentaryDto = new CommentaryDto(1L, "Test comment", bookDto);

        when(bookService.findById(anyLong())).thenReturn(Optional.of(bookDto));
        when(commentaryService.findByIdWithBook(anyLong())).thenReturn(Optional.of(commentaryDto));
    }

    @ParameterizedTest
    @MethodSource("getTestData")
    void shouldReturnExpectedStatus(
            String method, String url, Map<String, String> params,
            String username, String role,
            int status, String redirect) throws Exception {

        var request = method2RequestBuilder(method, url);
        if (params != null) {
            params.forEach(request::param);
        }
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
        Map<String, Function<String, MockHttpServletRequestBuilder>> methodMap = Map.of(
                "get", MockMvcRequestBuilders::get,
                "post", MockMvcRequestBuilders::post
        );

        return methodMap.get(method).apply(url);
    }

    public static Stream<Arguments> getTestData() {
        Map<String, String> saveParams = Map.of(
                "bookId", "1",
                "commentId", "0",
                "text", "New comment"
        );

        Map<String, String> deleteParams = Map.of(
                "commentId", "1",
                "bookId", "1"
        );

        return Stream.of(
                //GET
                Arguments.of("get", "/comment/add_to_book/1", null, "user", "USER", 200, null),
                Arguments.of("get", "/comment/add_to_book/1", null, null, null, 302, "http://localhost/login"),
                Arguments.of("get", "/comment/edit/1", null, "user", "USER", 200, null),
                Arguments.of("get", "/comment/edit/1", null, null, null, 302, "http://localhost/login"),

                //POST
                Arguments.of("post", "/comment/save", saveParams, "user", "USER", 302, "/book/1"),
                Arguments.of("post", "/comment/save", saveParams, null, null, 302, "http://localhost/login"),
                Arguments.of("post", "/comment/del", deleteParams, "user", "USER", 302, "/book/1"),
                Arguments.of("post", "/comment/del", deleteParams, null, null, 302, "http://localhost/login")
        );
    }


}
