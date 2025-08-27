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
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.services.AuthorService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    private List<AuthorDto> expectedAuthors;

    @BeforeEach
    void setUp() {
        AuthorDto author1 = new AuthorDto(1L, "Author1");
        AuthorDto author2 = new AuthorDto(2L, "Author2");
        expectedAuthors = List.of(author1, author2);
    }

    @Test
    @WithMockUser
    void getAllAuthorsWithAuthorizationShouldGetAccess() throws Exception {

        when(authorService.findAll()).thenReturn(expectedAuthors);

        mvc.perform(get("/all_authors"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllAuthorsWithoutAuthorizationShouldFail() throws Exception {

        when(authorService.findAll()).thenReturn(expectedAuthors);

        mvc.perform(get("/all_authors"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }
}
