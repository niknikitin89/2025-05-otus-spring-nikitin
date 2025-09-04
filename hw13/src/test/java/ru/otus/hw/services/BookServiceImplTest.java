package ru.otus.hw.services;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Сервис книг")
@DataJpaTest
@Import({BookServiceImpl.class})
@Transactional(propagation = Propagation.NEVER)
class BookServiceImplTest {

    private static final long BOOK_ID = 1L;

    private static final String BOOK_TITLE = "Title";

    private static final long INCORRECT_BOOK_ID = 99999L;

    private static final int BOOKS_NUMBER = 3;

    private static final long AUTHOR_ID = 2L;

    private static final long INCORRECT_AUTHOR_ID = 9999L;

    private static final long FIRST_GENRE_ID = 3L;

    private static final long SECOND_GENRE_ID = 4L;

    private static final long INCORRECT_GENRE_ID = 9999L;

    @Autowired
    private EntityManager em;

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private BookRepository bookRepository;

    @MockBean
    private AclServiceWrapperService aclService;

    @Test
    void testFindByIdShouldReturnBook() {
        var result = bookService.findById(BOOK_ID);

        assertThat(result).isPresent();
        BookDto book = result.get();

        assertThat(book.getTitle()).isNotBlank();
        assertThat(book.getAuthor()).isNotNull();
        assertThat(book.getAuthor().getFullName()).isNotBlank();
        assertThat(book.getGenres()).isNotNull();
    }

    @Test
    void testFindByIdShouldThrowThrowLazyInitializationExceptionOnCommentaryAccess() {
        var actualBookOption = bookService.findById(BOOK_ID);
        assertThat(actualBookOption).isPresent().isNotEmpty();
        var actualBook = actualBookOption.get();
        assertThat(actualBook.getAuthor()).isNotNull();
        assertThat(actualBook.getGenres()).isNotNull().isNotEmpty();

        assertThatNoException().isThrownBy(() -> actualBook.getAuthor().getFullName());
        assertThatNoException().isThrownBy(() -> actualBook.getGenres().get(0).getName());
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
    @Transactional
    void testInsertShouldAddBook() {

        Set<Long> genresIdsSet = Set.of(FIRST_GENRE_ID, SECOND_GENRE_ID);

        BookDto savedBook = bookService.insert(BOOK_TITLE, AUTHOR_ID, genresIdsSet);

        assertThat(savedBook).isNotNull()
                .matches(book -> book.getId() > 0);


    }

    @Test
    void testInsertWithNoGenresShouldThrowsIllegalArgumentException() {

        assertThatThrownBy(() -> bookService.insert(
                BOOK_TITLE,
                AUTHOR_ID,
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

        Set<Long> genresIdsSet = Set.of(INCORRECT_GENRE_ID);

        assertThatThrownBy(() -> bookService.insert(
                BOOK_TITLE,
                AUTHOR_ID,
                genresIdsSet))
                .isInstanceOf(EntityNotFoundException.class)
                .message().isEqualTo("One or all genres with ids %s not found".formatted(genresIdsSet));
    }

    @Test
    @Transactional
    void testUpdateShouldUpdateBook() {
        Set<Long> genresIdsSet = Set.of(FIRST_GENRE_ID, SECOND_GENRE_ID);
        var result = bookService.findById(BOOK_ID);
        assertThat(result).isPresent();

        BookDto actualBook = result.get();

        assertThat(actualBook.getTitle()).isNotEqualTo(BOOK_TITLE);
        assertThat(actualBook.getAuthor().getId()).isNotEqualTo(AUTHOR_ID);
        assertThat(actualBook.getGenres())
                .extracting(Genre::getId).doesNotContainAnyElementsOf(genresIdsSet);

        bookService.update(BOOK_ID, BOOK_TITLE, AUTHOR_ID, genresIdsSet);


        result = bookService.findById(BOOK_ID);
        assertThat(result).isPresent();
        actualBook = result.get();

        assertThat(actualBook.getTitle()).isEqualTo(BOOK_TITLE);
        assertThat(actualBook.getAuthor().getId()).isEqualTo(AUTHOR_ID);
        assertThat(actualBook.getGenres())
                .extracting(Genre::getId).containsExactlyInAnyOrderElementsOf(genresIdsSet);
    }

    @Test
    void testUpdateWithNoGenresShouldThrowsIllegalArgumentException() {

        assertThatThrownBy(() -> bookService.update(
                BOOK_ID,
                BOOK_TITLE,
                AUTHOR_ID,
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

        Set<Long> genresIdsSet = Set.of(INCORRECT_GENRE_ID);

        assertThatThrownBy(() -> bookService.update(
                BOOK_ID,
                BOOK_TITLE,
                AUTHOR_ID,
                genresIdsSet))
                .isInstanceOf(EntityNotFoundException.class)
                .message().isEqualTo("One or all genres with ids %s not found".formatted(genresIdsSet));
    }

    @Test
    @Transactional
    void testDeleteByIdShouldDeleteBook() {
        assertThat(bookService.findById(BOOK_ID)).isPresent();

        bookService.deleteById(BOOK_ID);

        assertThat(bookService.findById(BOOK_ID)).isEmpty();
    }

}
