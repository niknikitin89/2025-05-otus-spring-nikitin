package ru.otus.hw.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentaryDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
//import ru.otus.hw.services.CommentaryService;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentaryController.class)
class CommentaryControllerTest {

//    @Autowired
//    private MockMvc mvc;
//
//    @Autowired
//    private ObjectMapper mapper;
//
//    @MockBean
//    private CommentaryService commentaryService;
//
//    private final BookDto sampleBook = new BookDto(1L, "Sample Book", null, null);
//
//    @Test
//    void getCommentsForBookShouldReturnCommentsList() throws Exception {
//        CommentaryDto comment1 = new CommentaryDto(1L, "Comment 1", sampleBook);
//        CommentaryDto comment2 = new CommentaryDto(2L, "Comment 2", sampleBook);
//        List<CommentaryDto> expectedComments = List.of(comment1, comment2);
//
//        given(commentaryService.findByBookId(1L)).willReturn(expectedComments);
//
//        mvc.perform(get("/api/v1/books/1/comments"))
//                .andExpect(status().isOk())
//                .andExpect(content().json(mapper.writeValueAsString(expectedComments)));
//    }
//
//    @Test
//    void getCommentWithBookShouldReturnComment() throws Exception {
//        CommentaryDto expectedComment = new CommentaryDto(1L, "Test Comment", sampleBook);
//        given(commentaryService.findByIdWithBook(1L)).willReturn(java.util.Optional.of(expectedComment));
//
//        mvc.perform(get("/api/v1/comments/1"))
//                .andExpect(status().isOk())
//                .andExpect(content().json(mapper.writeValueAsString(expectedComment)));
//    }
//
//    @Test
//    void getCommentWithBookWithIncorrectIdShouldReturnNotFoundStatus() throws Exception {
//        given(commentaryService.findByIdWithBook(1L)).willThrow(EntityNotFoundException.class);
//
//        mvc.perform(get("/api/v1/comments/1"))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void addCommentShouldReturnCreatedComment() throws Exception {
//        CommentaryDto newComment = new CommentaryDto(0L, "New Comment", sampleBook);
//        CommentaryDto savedComment = new CommentaryDto(1L, "New Comment", sampleBook);
//
//        given(commentaryService.add(1L, "New Comment")).willReturn(savedComment);
//
//        mvc.perform(post("/api/v1/comments")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(newComment)))
//                .andExpect(status().isOk())
//                .andExpect(content().json(mapper.writeValueAsString(savedComment)));
//    }
//
//    @Test
//    void updateCommentShouldReturnNoContent() throws Exception {
//        CommentaryDto updatedComment = new CommentaryDto(1L, "Updated Comment", sampleBook);
//
//        mvc.perform(put("/api/v1/comments/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(updatedComment)))
//                .andExpect(status().isOk());
//
//        verify(commentaryService).update(1L, "Updated Comment");
//    }
//
//    @Test
//    void deleteCommentShouldReturnNoContent() throws Exception {
//        mvc.perform(delete("/api/v1/comments/1"))
//                .andExpect(status().isOk());
//
//        verify(commentaryService).deleteById(1L);
//    }

}