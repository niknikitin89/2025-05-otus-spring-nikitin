package ru.otus.hw.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.BookService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookService bookService;

    @Test
    void getAllBooksShouldReturnListOfBooks() throws Exception {
        BookDto book1 = new BookDto(1L, "Book 1", null, null);
        BookDto book2 = new BookDto(2L, "Book 2", null, null);
        List<BookDto> expectedBooks = List.of(book1, book2);

        given(bookService.findAll()).willReturn(expectedBooks);

        mvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedBooks)));
    }

    @Test
    void getBookWithExistingIdShouldReturnBook() throws Exception {
        BookDto expectedBook = new BookDto(1L, "Existing Book", null, null);
        given(bookService.findById(1L)).willReturn(java.util.Optional.of(expectedBook));

        mvc.perform(get("/api/v1/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedBook)));
    }

    @Test
    void getBookWithIncorrectIdShouldReturnNotFoundStatus() throws Exception {
        given(bookService.findById(1L)).willThrow(EntityNotFoundException.class);

        mvc.perform(get("/api/v1/books/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateBookShouldReturnUpdatedBook() throws Exception {
        BookDto updatedBook = new BookDto(1L, "Updated Book", null, null);
        given(bookService.save(any(BookDto.class))).willReturn(updatedBook);

        mvc.perform(put("/api/v1/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedBook)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(updatedBook)));
    }

    @Test
    void createBookShouldReturnCreatedBook() throws Exception {
        BookDto newBook = new BookDto(1L, "New Book", null, null);

        given(bookService.save(any(BookDto.class))).willReturn(newBook);

        mvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newBook)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(newBook)));
    }

    @Test
    void deleteBookShouldReturnCorrectResult() throws Exception {
        mvc.perform(delete("/api/v1/books/1"))
                .andExpect(status().isOk());

        verify(bookService).deleteById(1L);
    }

}
