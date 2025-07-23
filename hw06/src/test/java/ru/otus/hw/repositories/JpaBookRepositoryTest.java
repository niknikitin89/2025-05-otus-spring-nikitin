package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
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
@Import({JpaBookRepository.class,
        JpaGenreRepository.class})
class JpaBookRepositoryTest {

    private static final long FIRST_BOOK_ID = 1L;

    private static final int EXPECTED_NUMBER_OF_BOOKS = 3;

    @Autowired
    private BookRepository repository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private TestEntityManager em;

    private Author dbAuthor;

    private List<Genre> dbGenresList;

    @BeforeEach
    void setup() {
        dbAuthor = em.find(Author.class, 1);
        dbGenresList = new ArrayList<>();
        dbGenresList.add(em.find(Genre.class, 1));
        dbGenresList.add(em.find(Genre.class, 2));
    }

    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        var expectedBook = em.find(Book.class, FIRST_BOOK_ID);
        var actualBook = repository.findById(FIRST_BOOK_ID);
        assertThat(actualBook).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = repository.findAll();

        assertThat(actualBooks).hasSize(EXPECTED_NUMBER_OF_BOOKS)
                .allMatch(book -> book.getTitle() != null && !book.getTitle().isEmpty())
                .allMatch(book -> book.getAuthor() != null)
                .allMatch(book -> book.getGenres() != null && book.getGenres().size() > 0);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var expectedBook = new Book(0, "BookTitle_10500", dbAuthor, dbGenresList);

        var actualBook = repository.save(expectedBook);

        assertThat(actualBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(em.find(Book.class, actualBook.getId()))
                .isEqualTo(actualBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        var expectedBook = new Book(1L, "BookTitle_10500", dbAuthor, dbGenresList);

        assertThat(em.find(Book.class, expectedBook.getId()))
                .isNotNull()
                .isNotEqualTo(expectedBook);

        var returnedBook = repository.save(expectedBook);

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

        repository.deleteById(FIRST_BOOK_ID);

        assertThat(em.find(Book.class, FIRST_BOOK_ID))
                .isNull();
    }

}