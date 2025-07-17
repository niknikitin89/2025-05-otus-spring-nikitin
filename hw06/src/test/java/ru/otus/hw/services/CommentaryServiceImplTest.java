package ru.otus.hw.services;

import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Commentary;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentaryRepository;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaCommentaryRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Сервис комментариев")
@DataJpaTest
@Import({CommentaryServiceImpl.class,
        JpaCommentaryRepository.class,
        JpaBookRepository.class})
class CommentaryServiceImplTest {

    private static final long COMMENTARY_ID = 1L;

    private static final long ILLEGAL_COMMENTARY_ID = 9999L;

    private static final long BOOK_ID = 1L;

    private static final long ILLEGAL_BOOK_ID = 9999L;

    private static final int COMMENTARY_LIST_SIZE = 2;

    private static final String COMMENTARY_TEXT = "New commentary";

    @Autowired
    private TestEntityManager em;

    @Autowired
    private CommentaryService commentaryService;

    @Autowired
    private CommentaryRepository commentaryRepository;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void testFindByBookIdShouldReturnCommentariesList() {

        List<Commentary> expectedCommentList = commentaryRepository.findAllByBookId(BOOK_ID);

        List<Commentary> commentList = commentaryService.findByBookId(BOOK_ID);

        assertThat(commentList).isNotEmpty()
                .hasSize(COMMENTARY_LIST_SIZE)
                .usingRecursiveAssertion().isEqualTo(expectedCommentList);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void testFindByBookIdShouldThrowLazyInitializationExceptionOnBookAccess() {

        List<Commentary> commentList = commentaryService.findByBookId(BOOK_ID);

        assertThat(commentList).isNotEmpty()
                .hasSize(COMMENTARY_LIST_SIZE);

        assertThatThrownBy(() -> commentList.get(0).getBook().getTitle())
                .isInstanceOf(LazyInitializationException.class);
    }

    @Test
    void testAddShouldAddCommentToBook() {

        var newComment = commentaryService.add(BOOK_ID, COMMENTARY_TEXT);

        assertThat(newComment).isNotNull()
                .matches(comment -> comment.getText().equals(COMMENTARY_TEXT) && comment.getId() > 0);
    }

    @Test
    void testAddWithZeroBookIdShouldTrowsIllegalArgumentException() {
        assertThatThrownBy(() -> commentaryService.add(0, COMMENTARY_TEXT))
                .isInstanceOf(IllegalArgumentException.class)
                .message().isEqualTo("Book id cannot be 0");
    }

    @Test
    void testAddWithIllegalBookIdShouldTrowsIllegalArgumentException() {
        assertThatThrownBy(() -> commentaryService.add(ILLEGAL_BOOK_ID, COMMENTARY_TEXT))
                .isInstanceOf(IllegalArgumentException.class)
                .message().isEqualTo("Book %d not found".formatted(ILLEGAL_BOOK_ID));
    }

    @Test
    void testDeleteByIdShouldDeleteCommentary() {
        var commentary = em.find(Commentary.class, COMMENTARY_ID);
        assertThat(commentary).isNotNull()
                .matches(comment -> comment.getId() == COMMENTARY_ID);

        commentaryService.deleteById(COMMENTARY_ID);

        assertThat(em.find(Commentary.class, COMMENTARY_ID)).isNull();
    }

    @Test
    void testUpdateShouldUpdateCommentary() {
        var book = em.find(Book.class, BOOK_ID);
        var expectedCommentary = new Commentary(COMMENTARY_ID, "New Text", book);

        commentaryService.update(
                expectedCommentary.getId(), expectedCommentary.getText());

        assertThat(em.find(Commentary.class, COMMENTARY_ID))
                .isNotNull()
                .usingRecursiveComparison().isEqualTo(expectedCommentary);
    }

    @Test
    void testUpdateWithZeroIdShouldTrowsIllegalArgumentException() {
        assertThatThrownBy(() -> commentaryService.update(0, "New Text"))
                .isInstanceOf(IllegalArgumentException.class)
                .message().isEqualTo("Commentary id cannot be 0");
    }

    @Test
    void testUpdateWithIllegalIdShouldTrowsIllegalArgumentException() {
        assertThatThrownBy(() -> commentaryService.update(ILLEGAL_COMMENTARY_ID, "New Text"))
                .isInstanceOf(IllegalArgumentException.class)
                .message().isEqualTo("Commentary %d not found".formatted(ILLEGAL_COMMENTARY_ID));
    }
}
