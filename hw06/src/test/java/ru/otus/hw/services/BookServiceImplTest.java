package ru.otus.hw.services;

import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.repositories.JpaAuthorRepository;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaGenreRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Сервис книг")
@DataJpaTest
@Import({BookServiceImpl.class,
        JpaAuthorRepository.class,
        JpaGenreRepository.class,
        JpaBookRepository.class})
class BookServiceImplTest {

    private static final long BOOK_ID = 1L;

    private static final String BOOK_TITLE = "Title";

    private static final long INCORRECT_BOOK_ID = 99999L;

    private static final int BOOKS_NUMBER = 3;

    private static final long AUTHOR_ID = 1L;

    private static final long INCORRECT_AUTHOR_ID = 9999L;

    private static final long FIRST_GENRE_ID = 1L;

    private static final long SECOND_GENRE_ID = 2L;

    private static final long INCORRECT_GENRE_ID = 9999L;

    private Author dbAuthor;

    private List<Genre> dbGenres;

    @Autowired
    private TestEntityManager em;

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        dbAuthor = em.find(Author.class, AUTHOR_ID);
        dbGenres = new ArrayList<>();
        dbGenres.add(em.find(Genre.class, FIRST_GENRE_ID));
        dbGenres.add(em.find(Genre.class, SECOND_GENRE_ID));
    }


    @Test
    void testFindByIdShouldReturnBook() {
        var expectedBookOption = bookRepository.findById(BOOK_ID);
        assertThat(expectedBookOption).isNotNull().isPresent();
        var expectedBook = expectedBookOption.get();

        var actualBook = bookService.findById(BOOK_ID);

        assertThat(actualBook).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @Test
    void testFindByIdShouldThrowThrowLazyInitializationExceptionOnCommentaryAccess() {
        var actualBookOption = bookService.findById(BOOK_ID);
        assertThat(actualBookOption).isPresent().isNotEmpty();
        var actualBook = actualBookOption.get();
        em.clear();
        assertThat(actualBook.getAuthor()).isNotNull();
        assertThat(actualBook.getGenres()).isNotNull().isNotEmpty();
        assertThat(actualBook.getCommentaries()).isNotNull();//комменты не должны выгружаться, так как остаются в LAZY

        assertThatNoException().isThrownBy(() -> actualBook.getAuthor().getFullName());
        assertThatNoException().isThrownBy(() -> actualBook.getGenres().get(0).getName());
        assertThatThrownBy(() -> actualBook.getCommentaries().get(0).getText())
                .isInstanceOf(LazyInitializationException.class);
    }


    @Test
    void testFindByIdWithUncorrectIdShouldReturnEmptyOptional() {
        var actualBook = bookService.findById(INCORRECT_BOOK_ID);

        assertThat(actualBook).isEmpty();
    }

    @Test
    void testFindAllShouldGetAllBooks() {
        assertThat(bookService.findAll())
                .isNotEmpty().hasSize(BOOKS_NUMBER)
                .allMatch(book -> book.getId() > 0)
                .allMatch(book -> book.getAuthor() != null)
                .allMatch(book -> book.getTitle() != null)
                .allMatch(book -> book.getGenres() != null);
    }

    @Test
    void testInsertShouldAddBook() {

        Book expectedBook = new Book(0, BOOK_TITLE, dbAuthor, dbGenres);

        Set<Long> genresIdsSet = dbGenres.stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());

        Book savedBook = bookService.insert(
                expectedBook.getTitle(),
                expectedBook.getAuthor().getId(),
                genresIdsSet);

        assertThat(savedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedBook);
    }

    @Test
    void testInsertWithNoGenresShouldThrowsIllegalArgumentException() {

        assertThatThrownBy(() -> bookService.insert(
                BOOK_TITLE,
                dbAuthor.getId(),
                new HashSet<Long>()))
                .isInstanceOf(IllegalArgumentException.class)
                .message().isEqualTo("Genres ids must not be null");
    }

    @Test
    void testInsertWithIncorrectAuthorIdShouldThrowsEntityNotFoundException() {

        Set<Long> genresIdsSet = new HashSet<>();
        genresIdsSet.add(FIRST_GENRE_ID);

        assertThatThrownBy(() -> bookService.insert(
                BOOK_TITLE,
                INCORRECT_AUTHOR_ID,
                genresIdsSet))
                .isInstanceOf(EntityNotFoundException.class)
                .message().isEqualTo("Author with id %d not found".formatted(INCORRECT_AUTHOR_ID));
    }

    @Test
    void testInsertWithIncorrectGenreIdShouldThrowsEntityNotFoundException() {

        Set<Long> genresIdsSet = new HashSet<>();
        genresIdsSet.add(INCORRECT_GENRE_ID);

        assertThatThrownBy(() -> bookService.insert(
                BOOK_TITLE,
                dbAuthor.getId(),
                genresIdsSet))
                .isInstanceOf(EntityNotFoundException.class)
                .message().isEqualTo("One or all genres with ids %s not found".formatted(genresIdsSet));
    }

    @Test
    void testUpdateShouldUpdateBook() {
        var expectedBook = new Book(BOOK_ID, BOOK_TITLE, dbAuthor, dbGenres);

        Set<Long> genresIdsSet = dbGenres.stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());

        bookService.update(
                expectedBook.getId(), expectedBook.getTitle(),
                expectedBook.getAuthor().getId(), genresIdsSet);

        assertThat(em.find(Book.class, BOOK_ID))
                .isNotNull()
                .usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @Test
    void testUpdateWithNoGenresShouldThrowsIllegalArgumentException() {

        assertThatThrownBy(() -> bookService.update(
                BOOK_ID,
                BOOK_TITLE,
                dbAuthor.getId(),
                new HashSet<Long>()))
                .isInstanceOf(IllegalArgumentException.class)
                .message().isEqualTo("Genres ids must not be null");
    }

    @Test
    void testUpdateWithIncorrectAuthorIdShouldThrowsEntityNotFoundException() {

        Set<Long> genresIdsSet = new HashSet<>();
        genresIdsSet.add(FIRST_GENRE_ID);

        assertThatThrownBy(() -> bookService.update(
                BOOK_ID,
                BOOK_TITLE,
                INCORRECT_AUTHOR_ID,
                genresIdsSet))
                .isInstanceOf(EntityNotFoundException.class)
                .message().isEqualTo("Author with id %d not found".formatted(INCORRECT_AUTHOR_ID));
    }

    @Test
    void testUpdateWithIncorrectGenreIdShouldThrowsEntityNotFoundException() {

        Set<Long> genresIdsSet = new HashSet<>();
        genresIdsSet.add(INCORRECT_GENRE_ID);

        assertThatThrownBy(() -> bookService.update(
                BOOK_ID,
                BOOK_TITLE,
                dbAuthor.getId(),
                genresIdsSet))
                .isInstanceOf(EntityNotFoundException.class)
                .message().isEqualTo("One or all genres with ids %s not found".formatted(genresIdsSet));
    }

    @Test
    void testDeleteByIdShouldDeleteBook() {
        var book = em.find(Book.class, BOOK_ID);
        assertThat(book).isNotNull()
                .matches(comment -> comment.getId() == BOOK_ID);

        bookService.deleteById(BOOK_ID);

        assertThat(em.find(Book.class, BOOK_ID)).isNull();
    }

}
