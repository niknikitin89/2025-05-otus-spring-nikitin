package ru.otus.hw.services;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("Сервис книг")
@DataMongoTest
@Import({BookServiceImpl.class,
        BookConverter.class,
        AuthorConverter.class,
        GenreConverter.class,
        TestDataManager.class})
class BookServiceImplTest {

    private static final String BOOK_TITLE = "Title";

    private static final String BOOK_TITLE2 = "Title2";

    private static final String INCORRECT_BOOK_ID = "99999";

    private static final int BOOKS_NUMBER = 3;

    private static final String AUTHOR_NAME = "Author";

    private static final String AUTHOR_NAME2 = "Author2";

    private static final String INCORRECT_AUTHOR_ID = "9999";

    private static final String GENRE_NAME1 = "Genre1";

    private static final String GENRE_NAME2 = "Genre2";

    private static final String GENRE_NAME3 = "Genre3";

    private static final String INCORRECT_GENRE_ID = "9999";

    @Autowired
    private TestDataManager testDataManager;

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookConverter bookConverter;

    @Autowired
    private AuthorConverter authorConverter;

    @Autowired
    private GenreConverter genreConverter;

    @BeforeEach
    void refreshDb() {
        testDataManager.dropDb();
    }


    @Test
    void testFindByIdShouldReturnBook() {

        var book = testDataManager.generateBook();

        var result = bookService.findById(book.getId());

        assertThat(result).isPresent();
        BookDto actualBook = result.get();

        assertThat(actualBook.title()).isNotBlank();
        assertThat(actualBook.author()).isNotNull();
        assertThat(actualBook.author().getFullName()).isNotBlank();
        assertThat(actualBook.genres()).isNotNull();
    }

    @Test
    void testFindByIdWithUncorrectIdShouldReturnEmptyOptional() {
        var actualBook = bookService.findById(INCORRECT_BOOK_ID);

        assertThat(actualBook).isEmpty();
    }

    @Test
    void testFindAllShouldGetAllBooks() {

        for (int i = 0; i < BOOKS_NUMBER; i++) {
            testDataManager.generateBook();
        }

        assertThat(bookService.findAll())
                .isNotEmpty().hasSize(BOOKS_NUMBER)
                .allMatch(book -> !book.id().isEmpty() && !book.id().equals("0"))
                .allMatch(book -> book.author() != null)
                .allMatch(book -> book.title() != null)
                .allMatch(book -> book.genres() != null);
    }

    @Test
    void testInsertShouldAddBook() {

        var author = testDataManager.saveAuthor(AUTHOR_NAME);

        Set<String> genresIdsSet = new HashSet<>();
        var genre = testDataManager.saveGenre(GENRE_NAME1);
        genresIdsSet.add(genre.getId());
        genre = testDataManager.saveGenre(GENRE_NAME2);
        genresIdsSet.add(genre.getId());

        BookDto savedBook = bookService.insert(BOOK_TITLE, author.getId(), genresIdsSet);

        assertThat(savedBook).isNotNull()
                .matches(book -> !book.id().equals("0") && !book.id().isEmpty());

    }

    @Test
    void testInsertWithNoGenresShouldThrowsIllegalArgumentException() {

        var author = testDataManager.saveAuthor(AUTHOR_NAME);

        assertThatThrownBy(() -> bookService.insert(
                BOOK_TITLE,
                author.getId(),
                new HashSet<String>()))
                .isInstanceOf(IllegalArgumentException.class)
                .message().isEqualTo("Genres ids must not be null");
    }

    @Test
    void testInsertWithIncorrectAuthorIdShouldThrowsEntityNotFoundException() {

        Set<String> genresIdsSet = new HashSet<>();
        var genre = testDataManager.saveGenre(GENRE_NAME1);
        genresIdsSet.add(genre.getId());
        genre = testDataManager.saveGenre(GENRE_NAME2);
        genresIdsSet.add(genre.getId());

        assertThatThrownBy(() -> bookService.insert(
                BOOK_TITLE,
                INCORRECT_AUTHOR_ID,
                genresIdsSet))
                .isInstanceOf(EntityNotFoundException.class)
                .message().isEqualTo("Author with id %s not found".formatted(INCORRECT_AUTHOR_ID));
    }

    @Test
    void testInsertWithIncorrectGenreIdShouldThrowsEntityNotFoundException() {
        var author = testDataManager.saveAuthor(AUTHOR_NAME);
        Set<String> genresIdsSet = Set.of(INCORRECT_GENRE_ID);

        assertThatThrownBy(() -> bookService.insert(
                BOOK_TITLE,
                author.getId(),
                genresIdsSet))
                .isInstanceOf(EntityNotFoundException.class)
                .message().isEqualTo("One or all genres with ids %s not found".formatted(genresIdsSet));
    }

    @Test
    void testUpdateShouldUpdateBook() {

        var author = testDataManager.saveAuthor(AUTHOR_NAME2);

        List<Genre> genres = new ArrayList<>();
        var genre = testDataManager.saveGenre(GENRE_NAME3);
        genres.add(genre);

        var actualBook = testDataManager.generateBook();
        assertThat(actualBook).isNotNull();
        assertThat(actualBook.getId()).isNotEmpty();

        assertThat(actualBook.getTitle()).isNotEqualTo(BOOK_TITLE2);
        assertThat(actualBook.getAuthor().getId()).isNotEqualTo(author.getId());
        assertThat(actualBook.getGenres()).doesNotContainAnyElementsOf(genres);

        Set<String> genresIds = genres.stream().map(Genre::getId).collect(Collectors.toSet());

        bookService.update(actualBook.getId(), BOOK_TITLE2, author.getId(), genresIds);

        var result = bookService.findById(actualBook.getId());
        assertThat(result).isPresent();
        var updatedBook = result.get();

        assertThat(updatedBook.title()).isEqualTo(BOOK_TITLE2);
        assertThat(updatedBook.author().getId()).isEqualTo(author.getId());
        assertThat(actualBook.getGenres()).doesNotContainAnyElementsOf(genres);
    }

    @Test
    void testUpdateWithNoGenresShouldThrowsIllegalArgumentException() {

        var book = testDataManager.generateBook();

        assertThatThrownBy(() -> bookService.update(
                book.getId(),
                BOOK_TITLE,
                book.getAuthor().getId(),
                new HashSet<String>()))
                .isInstanceOf(IllegalArgumentException.class)
                .message().isEqualTo("Genres ids must not be null");
    }

    @Test
    void testUpdateWithIncorrectAuthorIdShouldThrowsEntityNotFoundException() {
        var book = testDataManager.generateBook();

        Set<String> genresIdsSet = book.getGenres().stream()
                .map(Genre::getId).collect(Collectors.toSet());

        assertThatThrownBy(() -> bookService.update(
                book.getId(),
                BOOK_TITLE,
                INCORRECT_AUTHOR_ID,
                genresIdsSet))
                .isInstanceOf(EntityNotFoundException.class)
                .message().isEqualTo("Author with id %s not found".formatted(INCORRECT_AUTHOR_ID));
    }

    @Test
    void testUpdateWithIncorrectGenreIdShouldThrowsEntityNotFoundException() {

        var book = testDataManager.generateBook();

        Set<String> genresIdsSet = Set.of(INCORRECT_GENRE_ID);

        assertThatThrownBy(() -> bookService.update(
                book.getId(),
                BOOK_TITLE,
                book.getAuthor().getId(),
                genresIdsSet))
                .isInstanceOf(EntityNotFoundException.class)
                .message().isEqualTo("One or all genres with ids %s not found".formatted(genresIdsSet));
    }

    @Test
    void testDeleteByIdShouldDeleteBook() {

        var book = testDataManager.generateBook();

        assertThat(bookService.findById(book.getId())).isPresent();

        bookService.deleteById(book.getId());

        assertThat(bookService.findById(book.getId())).isEmpty();
    }

}
