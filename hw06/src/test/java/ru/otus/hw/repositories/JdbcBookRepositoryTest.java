package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с книгами ")
@DataJpaTest
@Import({JdbcBookRepository.class})
class JdbcBookRepositoryTest {

    private final long FIRST_BOOK_ID = 1L;

    private final int EXPECTED_NUMBER_OF_BOOKS = 3;

    @Autowired
    private JdbcBookRepository repositoryJdbc;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        var expectedBook = em.find(Book.class, FIRST_BOOK_ID);
        var actualBook = repositoryJdbc.findById(FIRST_BOOK_ID);
        assertThat(actualBook).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = repositoryJdbc.findAll();

        assertThat(actualBooks).hasSize(EXPECTED_NUMBER_OF_BOOKS)
                .allMatch(book -> book.getTitle() != null && !book.getTitle().isEmpty())
                .allMatch(book -> book.getAuthor() != null)
                .allMatch(book -> book.getGenres() != null && book.getGenres().size() > 0)
                .allMatch(book -> book.getCommentaries() != null && book.getCommentaries().size() > 0);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var author = em.find(Author.class, 1);
        List<Genre> genresList = new ArrayList<>();
        genresList.add(em.find(Genre.class, 1));
        genresList.add(em.find(Genre.class, 2));

        var expectedBook = new Book(0, "BookTitle_10500", author, genresList);

        var actualBook = repositoryJdbc.save(expectedBook);

        assertThat(actualBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(em.find(Book.class, actualBook.getId()))
                .isEqualTo(actualBook);

        System.out.println(actualBook.getId());
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        var author = em.find(Author.class, 1);
        List<Genre> genresList = new ArrayList<>();
        genresList.add(em.find(Genre.class, 1));
        genresList.add(em.find(Genre.class, 2));

        var expectedBook = new Book(1L, "BookTitle_10500", author, genresList);

        assertThat(em.find(Book.class, expectedBook.getId()))
                .isNotNull()
                .isNotEqualTo(expectedBook);

        var returnedBook = repositoryJdbc.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().isEqualTo(expectedBook);

        assertThat(em.find(Book.class, returnedBook.getId()))
                .isNotNull()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        assertThat(em.find(Book.class, FIRST_BOOK_ID))
                .isNotNull()
                .matches(book -> book.getId() == FIRST_BOOK_ID);

        repositoryJdbc.deleteById(FIRST_BOOK_ID);

        assertThat(em.find(Book.class, FIRST_BOOK_ID))
                .isNull();
    }

}