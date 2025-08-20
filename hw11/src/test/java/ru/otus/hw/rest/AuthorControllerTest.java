package ru.otus.hw.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

//    @Autowired
//    private MockMvc mvc;
//
//    @Autowired
//    private ObjectMapper mapper;
//
//
//    @Test
//    void getAllAuthorsShouldReturnListOfAuthors() throws Exception {
//        AuthorDto author1 = new AuthorDto(1L, "Author1");
//        AuthorDto author2 = new AuthorDto(2L, "Author2");
//        List<AuthorDto> expectedAuthors = List.of(author1, author2);
//
//        when(authorService.findAll()).thenReturn(expectedAuthors);
//
//        mvc.perform(get("/api/v1/authors"))
//                .andExpect(status().isOk())
//                .andExpect(content().json(mapper.writeValueAsString(expectedAuthors)));
//    }
//
//    @Test
//    void getAllAuthorsWhenNoAuthorsShouldReturnEmptyList() throws Exception {
//        List<AuthorDto> expectedEmptyList = List.of();
//
//        when(authorService.findAll()).thenReturn(expectedEmptyList);
//
//        mvc.perform(get("/api/v1/authors"))
//                .andExpect(status().isOk())
//                .andExpect(content().json(mapper.writeValueAsString(expectedEmptyList)));
//    }

}
