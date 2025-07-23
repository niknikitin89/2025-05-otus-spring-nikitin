package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Commentary;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с комментариями ")
@DataJpaTest
@Import({JpaCommentaryRepository.class})
class JpaCommentaryRepositoryTest {

    private static final long FIRST_COMMENTARY_ID = 1L;

    private static final long BOOK_ID = 3L;

    private static final int EXPECTED_COMMENTARY_NUMBER = 3;

    @Autowired
    private CommentaryRepository repository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать комментарий по id")
    @Test
    void testFindByIdShouldReturnCorrectCommentary() {

        var expectedComment = em.find(Commentary.class, FIRST_COMMENTARY_ID);

        var actualComment = repository.findById(FIRST_COMMENTARY_ID);

        assertThat(actualComment).isPresent().get().isEqualTo(expectedComment);
    }

    @DisplayName("должен загружать список комментариев по Id книги")
    @Test
    void testFindAllByBookIdShouldReturnCorrectCommentariesList() {

        List<Commentary> commentList = repository.findAllByBookId(BOOK_ID);

        assertThat(commentList).hasSize(EXPECTED_COMMENTARY_NUMBER)
                .allMatch(commentary -> commentary.getBook().getId() == BOOK_ID)
                .allMatch(commentary -> !commentary.getText().isEmpty());
    }

    @DisplayName("должен сохранить новый комментарий")
    @Test
    void testSaveShouldSaveNewCommentary() {
        var book = em.find(Book.class, BOOK_ID);
        var expectedComment = new Commentary(0,"New Commentary", book);

        var actualComment = repository.save(expectedComment);

        assertThat(actualComment).isNotNull()
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);
    }

    @DisplayName("должен сохранить измененный комментарий")
    @Test
    void testSaveShouldSaveChangedCommentary() {
        var expectedComment = new Commentary(FIRST_COMMENTARY_ID, "Changed",
                em.find(Book.class, BOOK_ID));

        assertThat(em.find(Commentary.class, FIRST_COMMENTARY_ID))
                .isNotNull().isNotEqualTo(expectedComment);

        var actualComment = repository.save(expectedComment);

        assertThat(actualComment).isNotNull()
                .usingRecursiveComparison().isEqualTo(expectedComment);
    }

    @DisplayName("должен удалиться комментарий по Id")
    @Test
    void testDeleteByIdShouldDeleteCommentary() {
        assertThat(em.find(Commentary.class, FIRST_COMMENTARY_ID))
                .isNotNull()
                .matches(comment -> comment.getId() == FIRST_COMMENTARY_ID);

        repository.deleteById(FIRST_COMMENTARY_ID);

        assertThat(em.find(Commentary.class,FIRST_COMMENTARY_ID))
                .isNull();
    }

}
