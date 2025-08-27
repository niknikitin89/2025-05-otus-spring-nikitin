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
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentaryService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    private BookDto bookDto;

    @BeforeEach
    void setUp() {
        bookDto = new BookDto(1L, "Test Book",
                new Author(1L, "Author"),
                List.of(new Genre(1L, "Genre1")));
    }

    @Test
    @WithMockUser
    void allBooksPageWithAuthShouldReturnOk() throws Exception {
        when(bookService.findAll()).thenReturn(List.of(bookDto));

        mvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    void allBooksPageWithoutAuthShouldRedirect() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    void bookPageWithAuthShouldReturnOk() throws Exception {
        when(bookService.findById(anyLong())).thenReturn(Optional.of(bookDto));
        when(commentaryService.findByBookId(anyLong())).thenReturn(List.of());

        mvc.perform(get("/book/1"))
                .andExpect(status().isOk());
    }

    @Test
    void bookPageWithoutAuthShouldRedirect() throws Exception {
        mvc.perform(get("/book/1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    void editBookPageWithAuthShouldReturnOk() throws Exception {
        when(bookService.findById(anyLong())).thenReturn(Optional.of(bookDto));
        when(authorService.findAll()).thenReturn(List.of());
        when(genreService.findAll()).thenReturn(List.of());

        mvc.perform(get("/book/1/edit"))
                .andExpect(status().isOk());
    }

    @Test
    void editBookPageWithoutAuthShouldRedirect() throws Exception {
        mvc.perform(get("/book/1/edit"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    void saveBookWithAuthShouldRedirect() throws Exception {
        mvc.perform(post("/book/save")
                        .param("id", String.valueOf(1L))
                        .param("title", "Updated Title")
                        .param("author.id", String.valueOf(1L))
                        .param("genres[0].id", String.valueOf(1L)))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void saveBookWithoutAuthShouldRedirect() throws Exception {
        mvc.perform(post("/book/save")
                        .param("id", String.valueOf(1L))
                        .param("title", "Updated Title")
                        .param("author.id", String.valueOf(1L))
                        .param("genres[0].id", String.valueOf(1L)))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    void addBookWithAuthShouldReturnOk() throws Exception {
        when(authorService.findAll()).thenReturn(List.of());
        when(genreService.findAll()).thenReturn(List.of());

        mvc.perform(get("/add_book"))
                .andExpect(status().isOk());
    }

    @Test
    void addBookWithoutAuthShouldRedirect() throws Exception {
        mvc.perform(get("/add_book"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    void deleteBookWithAuthShouldRedirect() throws Exception {
        mvc.perform(post("/book_delete")
                        .param("bookId", "1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void deleteBookWithoutAuthShouldRedirect() throws Exception {
        mvc.perform(post("/book_delete")
                        .param("bookId", "1"))
                .andExpect(status().is3xxRedirection());
    }
}
