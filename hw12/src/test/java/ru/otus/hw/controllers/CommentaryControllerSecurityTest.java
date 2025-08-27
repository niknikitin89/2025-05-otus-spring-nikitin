package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentaryDto;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentaryService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    private final BookDto bookDto = new BookDto(1L, "Test Book", null, null);
    private final CommentaryDto commentaryDto = new CommentaryDto(1L, "Test comment", bookDto);

    @Test
    @WithMockUser
    void enterCommentToBookWithAuthShouldReturnOk() throws Exception {
        when(bookService.findById(anyLong())).thenReturn(Optional.of(bookDto));

        mvc.perform(get("/comment/add_to_book/1"))
                .andExpect(status().isOk());
    }

    @Test
    void enterCommentToBookWithoutAuthShouldRedirect() throws Exception {
        mvc.perform(get("/comment/add_to_book/1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    void saveCommentWithAuthShouldRedirect() throws Exception {
        mvc.perform(post("/comment/save")
                        .param("bookId", "1")
                        .param("commentId", "0")
                        .param("text", "New comment"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void saveCommentWithoutAuthShouldRedirect() throws Exception {
        mvc.perform(post("/comment/save")
                        .param("bookId", "1")
                        .param("commentId", "0")
                        .param("text", "New comment"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    void deleteCommentWithAuthShouldRedirect() throws Exception {
        mvc.perform(post("/comment/del")
                        .param("commentId", "1")
                        .param("bookId", "1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void deleteCommentWithoutAuthShouldRedirect() throws Exception {
        mvc.perform(post("/comment/del")
                        .param("commentId", "1")
                        .param("bookId", "1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    void editCommentWithAuthShouldReturnOk() throws Exception {
        when(commentaryService.findByIdWithBook(anyLong())).thenReturn(Optional.of(commentaryDto));

        mvc.perform(get("/comment/edit/1"))
                .andExpect(status().isOk());
    }

    @Test
    void editCommentWithoutAuthShouldRedirect() throws Exception {
        mvc.perform(get("/comment/edit/1"))
                .andExpect(status().is3xxRedirection());
    }
}
