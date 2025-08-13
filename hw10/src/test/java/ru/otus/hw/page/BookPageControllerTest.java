package ru.otus.hw.page;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentaryDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentaryService;
import ru.otus.hw.services.GenreService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@WebMvcTest(controllers = BookPageController.class)
class BookPageControllerTest {

    private static final long BOOK_ID = 1L;
    private static final long AUTHOR_ID = 1L;
    private static final long GENRE_ID = 1L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookPageController bookPageController;

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
    private static List<AuthorDto> authorDtoList;
    private static List<GenreDto> genreDtoList;


    @BeforeAll
    static void setUp() {
        bookDto = new BookDto(
                BOOK_ID,
                "Title_1",
                new AuthorDto(AUTHOR_ID, "Author_1"),
                List.of(new GenreDto(GENRE_ID, "Genre_1")));

        bookDtoList = List.of(bookDto);

        commentaryDtoList = new ArrayList<>();
        commentaryDtoList.add(new CommentaryDto(1L, "Text", null));

        authorDtoList = new ArrayList<>();
        authorDtoList.add(new AuthorDto(AUTHOR_ID, "Author_1"));

        genreDtoList = new ArrayList<>();
        genreDtoList.add(new GenreDto(GENRE_ID, "Genre_1"));
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
    void testBookPageWhenBookNotExistsShouldReturnErrorPage() throws Exception {
        given(bookService.findById(BOOK_ID)).willReturn(Optional.empty());

        mockMvc.perform(get("/book/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("errorPage"));
    }

    @Test
    void testEditBookPageShouldGetCorrectResult() throws Exception {
        given(bookService.findById(BOOK_ID)).willReturn(Optional.of(bookDto));
        given(authorService.findAll()).willReturn(authorDtoList);
        given(genreService.findAll()).willReturn(genreDtoList);

        mockMvc.perform(get("/book/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookEditPage"))
                .andExpect(model().attribute("book", bookDto))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("allGenres"));
    }

    @Test
    void testEditBookPageWhenBookNotExistsShouldReturnErrorPage() throws Exception {
        given(bookService.findById(BOOK_ID)).willReturn(Optional.empty());

        mockMvc.perform(get("/book/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("errorPage"));
    }

    @Test
    void testSaveBookShouldCreateNewBook() throws Exception {
        mockMvc.perform(post("/book/save")
                        .param("title", "New Title")
                        .param("author.id", String.valueOf(AUTHOR_ID))
                        .param("genres[0].id", String.valueOf(GENRE_ID)))
                .andExpect(view().name("redirect:/"));

        verify(bookService, times(1)).save(any());
    }

    @Test
    void testSaveBookShouldUpdateExistingBook() throws Exception {

        mockMvc.perform(post("/book/save")
                        .param("id", String.valueOf(BOOK_ID))
                        .param("title", "Updated Title")
                        .param("author.id", String.valueOf(AUTHOR_ID))
                        .param("genres[0].id", String.valueOf(GENRE_ID)))
                .andExpect(view().name("redirect:/"));

        verify(bookService, times(1)).save(any());
    }

    @Test
    void testAddBookPageShouldGetCorrectResult() throws Exception {
        given(authorService.findAll()).willReturn(authorDtoList);
        given(genreService.findAll()).willReturn(genreDtoList);

        mockMvc.perform(get("/add_book"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookEditPage"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("allGenres"));
    }

    @Test
    void testDeleteBookShouldRedirectToHomePage() throws Exception {
        doNothing().when(bookService).deleteById(BOOK_ID);

        mockMvc.perform(post("/book_delete")
                        .param("bookId", String.valueOf(BOOK_ID)))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        verify(bookService).deleteById(BOOK_ID);
    }

}
