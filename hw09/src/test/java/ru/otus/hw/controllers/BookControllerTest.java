package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentaryDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentaryService;
import ru.otus.hw.services.GenreService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@WebMvcTest(controllers = BookController.class)
class BookControllerTest {

    private static final long BOOK_ID = 1L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookController bookController;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private CommentaryService commentaryService;

    private static List<BookDto> bookDtoList;
    private static BookDto bookDto;
    private static List<CommentaryDto> commentaryDtoList;


    @BeforeAll
    static void setUp() {
        bookDto = new BookDto(BOOK_ID,"Title_1",new Author(1,"Author_1"),null);

        bookDtoList = new ArrayList<>();
        bookDtoList.add(bookDto);

        commentaryDtoList = new ArrayList<>();
        commentaryDtoList.add(new CommentaryDto(1L,"Text",null));
    }

    @Test
    void testAllBooksPageShouldGetCorrectResult() throws Exception {

        given(bookService.findAll()).willReturn(bookDtoList);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("allBooksPage"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", bookDtoList));

    }

    @Test
    void testBookPageShouldGetCorrectResult() throws Exception {

        given(bookService.findById(BOOK_ID)).willReturn(Optional.of(bookDto));
        given(commentaryService.findByBookId(BOOK_ID)).willReturn(commentaryDtoList);

        mockMvc.perform(get("/book/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookPage"))
                .andExpect(model().attribute("book", bookDto))
                .andExpect(model().attribute("commentary", commentaryDtoList));
    }

    @Test
    void testeditBookPageShouldGetCorrectResult(){
        given(bookService.findById(BOOK_ID)).willReturn(Optional.of(bookDto));
    }

}
