package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentaryService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@Import(SecurityConfiguration.class)
class BookControllerSecurityTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private CommentaryService commentaryService;

    @BeforeEach
    void setUp() {
        BookDto bookDto = new BookDto(1L, "Test Book",
                new Author(1L, "Author"),
                List.of(new Genre(1L, "Genre1")));


        when(bookService.findAll()).thenReturn(List.of(bookDto));
        when(bookService.findById(anyLong())).thenReturn(Optional.of(bookDto));
        when(commentaryService.findByBookId(anyLong())).thenReturn(List.of());
        when(authorService.findAll()).thenReturn(List.of());
        when(genreService.findAll()).thenReturn(List.of());

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

    private MockHttpServletRequestBuilder method2RequestBuilder(
            String method, String url) {

        Map<String, Function<String, MockHttpServletRequestBuilder>> methodMap =
                Map.of("get", MockMvcRequestBuilders::get,
                        "post", MockMvcRequestBuilders::post);

        return methodMap.get(method).apply(url);
    }

    public static Stream<Arguments> getTestData() {
        Map<String, String> deleteParams =
                Map.of("bookId", "1");

        Map<String, String> saveParams =
                Map.of("id", String.valueOf(1L),
                       "title", "Updated Title",
                       "author.id", String.valueOf(1L),
                       "genres[0].id", String.valueOf(1L));

        return Stream.of(
                //GET
                Arguments.of("get", "/", null, "username", "USER", 200, null),
                Arguments.of("get", "/", null, null, null, 302, null),
                Arguments.of("get", "/book/1", null, "username", "USER", 200, null),
                Arguments.of("get", "/book/1", null, null, null, 302, null),
                Arguments.of("get", "/book/1/edit", null, "username", "USER", 200, null),
                Arguments.of("get", "/book/1/edit", null, null, null, 302, null),
                Arguments.of("get", "/add_book", null, "username", "USER", 200, null),
                Arguments.of("get", "/add_book", null, null, null, 302, null),

                //POST
                Arguments.of("post", "/book_delete", deleteParams, "username", "USER", 0, "/"),
                Arguments.of("post", "/book_delete", deleteParams, null, null, 302, null),
                Arguments.of("post", "/book/save", saveParams, "username", "USER", 0, "/"),
                Arguments.of("post", "/book/save", saveParams, null, null, 302, null)

        );
    }
}
