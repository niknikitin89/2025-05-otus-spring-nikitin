package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Commentary;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentaryRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("Сервис комментариев")
@DataMongoTest
@Import({CommentaryServiceImpl.class, TestDataManager.class})
class CommentaryServiceImplTest {

    private static final String ILLEGAL_COMMENTARY_ID = "9999";

    private static final String ILLEGAL_BOOK_ID = "999";

    private static final int COMMENTARY_LIST_SIZE = 3;

    private static final String COMMENTARY_TEXT = "New commentary";

    @Autowired
    private TestDataManager testDataManager;

    @Autowired
    private CommentaryService commentaryService;

    @Autowired
    private CommentaryRepository commentaryRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private GenreRepository genreRepository;

    @BeforeEach
    void refreshDb() {
        testDataManager.dropDb();
    }

    @Test
    void testFindByBookIdShouldReturnCommentariesList() {

        var book = testDataManager.generateBook();

        List<Commentary> expectedCommentList = new ArrayList<>();
        for (int i = 0; i < COMMENTARY_LIST_SIZE; i++) {
            expectedCommentList.add(testDataManager.saveCommentary("Comment", book));
        }

        List<Commentary> commentList = commentaryService.findByBookId(book.getId());

        assertThat(commentList).isNotEmpty()
                .hasSize(COMMENTARY_LIST_SIZE)
                .usingRecursiveAssertion().isEqualTo(expectedCommentList);
    }


    @Test
    void testAddShouldAddCommentToBook() {

        var book = testDataManager.generateBook();

        var newComment = commentaryService.add(book.getId(), COMMENTARY_TEXT);

        assertThat(newComment).isNotNull()
                .matches(comment -> comment.getText().equals(COMMENTARY_TEXT)
                        && !comment.getId().isEmpty()
                        && !comment.getId().equals("0"));
    }

    @Test
    void testAddWithZeroBookIdShouldTrowsIllegalArgumentException() {
        assertThatThrownBy(() -> commentaryService.add("0", COMMENTARY_TEXT))
                .isInstanceOf(IllegalArgumentException.class)
                .message().isEqualTo("Book id cannot be empty");
    }

    @Test
    void testAddWithIllegalBookIdShouldTrowsIllegalArgumentException() {
        assertThatThrownBy(() -> commentaryService.add(ILLEGAL_BOOK_ID, COMMENTARY_TEXT))
                .isInstanceOf(IllegalArgumentException.class)
                .message().isEqualTo("Book %s not found".formatted(ILLEGAL_BOOK_ID));
    }

    @Test
    void testDeleteByIdShouldDeleteCommentary() {
        var book = testDataManager.generateBook();
        var comment = testDataManager.saveCommentary("Comment", book);

        assertThat(commentaryService.findById(comment.getId())).isPresent();

        commentaryService.deleteById(comment.getId());

        assertThat(commentaryService.findById(comment.getId())).isEmpty();
    }

    @Test
    void testUpdateShouldUpdateCommentary() {
        var book = testDataManager.generateBook();
        var commentId = testDataManager.saveCommentary("Comment", book).getId();

        var result = commentaryService.findById(commentId);

        assertThat(result).isPresent();
        assertThat(result.get().getText()).isNotEqualTo(COMMENTARY_TEXT);

        commentaryService.update(commentId, COMMENTARY_TEXT);

        result = commentaryService.findById(commentId);
        assertThat(result).isPresent();
        assertThat(result.get().getText()).isEqualTo(COMMENTARY_TEXT);
    }

    @Test
    void testUpdateWithZeroIdShouldTrowsIllegalArgumentException() {
        assertThatThrownBy(() -> commentaryService.update("0", COMMENTARY_TEXT))
                .isInstanceOf(IllegalArgumentException.class)
                .message().isEqualTo("Commentary id cannot be empty");
    }

    @Test
    void testUpdateWithIllegalIdShouldTrowsIllegalArgumentException() {
        assertThatThrownBy(() -> commentaryService.update(ILLEGAL_COMMENTARY_ID, COMMENTARY_TEXT))
                .isInstanceOf(IllegalArgumentException.class)
                .message().isEqualTo("Commentary %s not found".formatted(ILLEGAL_COMMENTARY_ID));
    }
}
