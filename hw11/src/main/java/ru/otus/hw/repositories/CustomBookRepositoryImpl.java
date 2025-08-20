package ru.otus.hw.repositories;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.validators.BookValidator;

import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class CustomBookRepositoryImpl implements CustomBookRepository {

    private final R2dbcEntityTemplate entityTemplate;

    private final GenreRepository genreRepository;

    private final BookValidator bookValidator;

    @Override
    public Mono<Book> findByIdWithAuthorAndGenres(long id) {
        String sql = """
                SELECT b.id as book_id, b.title, 
                       a.id as author_id, a.full_name,
                       g.id as genre_id, g.name as genre_name
                FROM books b
                JOIN authors a ON b.author_id = a.id
                LEFT JOIN books_genres bg ON b.id = bg.book_id
                LEFT JOIN genres g ON bg.genre_id = g.id
                where b.id = :id
                """;

        return entityTemplate.getDatabaseClient()
                .sql(sql)
                .bind("id", id)
                .map(this::mapToBookWithGenre)
                .all()
                .collectList()
                .flatMap(this::agregateBookWithGenres);
    }

    private BookWithGenre mapToBookWithGenre(Row row, RowMetadata meta) {
        Long bookId = row.get("book_id", Long.class);
        String title = row.get("title", String.class);
        Author author = new Author(
                row.get("author_id", Long.class),
                row.get("full_name", String.class)
        );

        Genre genre = null;
        if (row.get("genre_id", Long.class) != null) {
            genre = new Genre(
                    row.get("genre_id", Long.class),
                    row.get("genre_name", String.class)
            );
        }
        return new BookWithGenre(bookId, title, author, genre);
    }

    private Mono<Book> agregateBookWithGenres(List<BookWithGenre> booksWithGenres) {
        if (booksWithGenres.isEmpty()) {
            return Mono.empty();
        }

        BookWithGenre first = booksWithGenres.get(0);
        List<Genre> genres = booksWithGenres.stream()
                .map(BookWithGenre::genre)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        Book book = new Book();
        book.setId(first.bookId());
        book.setTitle(first.title());
        book.setAuthorId(first.author().getId());
        book.setAuthor(first.author());
        book.setGenres(genres);

        return Mono.just(book);
    }

    @Override
    public Flux<Book> findAllWithAuthorsAndGenres() {
        String sql = """
                SELECT b.id as book_id, b.title, 
                       a.id as author_id, a.full_name,
                       g.id as genre_id, g.name as genre_name
                FROM books b
                JOIN authors a ON b.author_id = a.id
                LEFT JOIN books_genres bg ON b.id = bg.book_id
                LEFT JOIN genres g ON bg.genre_id = g.id
                """;

        return entityTemplate.getDatabaseClient()
                .sql(sql)
                .map(this::mapToBookWithGenre)
                .all()
                .groupBy(BookWithGenre::bookId)
                .flatMap(group -> group.collectList()
                        .map(this::mapBookWithGenreToBook)
                );
    }

    @Override
    public Mono<Book> saveBookWithAuthorsAndGenres(Book book) {
        //Создаем или обновляем?
        boolean isInsert = book.getId() == null;

        if (isInsert) {
            return insertBook(book);
        } else {
            return updateBook(book);
        }
    }

    private Book mapBookWithGenreToBook(List<BookWithGenre> items) {
        // Первый элемент гарантированно существует (groupBy создает непустые группы)
        BookWithGenre firstItem = items.get(0);

        List<Genre> genres = items.stream()
                .map(BookWithGenre::genre)
                .filter(Objects::nonNull)
                .toList();

        return new Book(
                firstItem.bookId(),
                firstItem.title(),
                firstItem.author().getId(),
                firstItem.author(),
                genres
        );
    }

    private Mono<Book> insertBook(Book book) {
        return bookValidator.validateBook(book)
                .then(entityTemplate.insert(book))
                .flatMap(savedBook -> saveBookGenres(savedBook, book.getGenres()))
                .flatMap(savedBook -> this.findByIdWithAuthorAndGenres(savedBook.getId()));
    }

    private Mono<Book> updateBook(Book book) {
        return bookValidator.validateBook(book)
                .then(entityTemplate.update(book))
                .flatMap(savedBook -> updateBookGenres(savedBook, book.getGenres()))
                .flatMap(savedBook -> this.findByIdWithAuthorAndGenres(savedBook.getId()));
    }

    private Mono<Book> saveBookGenres(Book book, List<Genre> genres) {
        if (genres == null || genres.isEmpty()) {
            return Mono.just(book);
        }

        List<Long> genreIds = genres.stream()
                .map(Genre::getId)
                .filter(Objects::nonNull)
                .toList();

        return saveBookGenreIds(book.getId(), genreIds)
                .then(Mono.just(book));
    }

    private Mono<Book> updateBookGenres(Book book, List<Genre> genres) {
        if (genres == null || genres.isEmpty()) {
            return Mono.just(book);
        }

        List<Long> genreIds = genres.stream()
                .map(Genre::getId)
                .filter(Objects::nonNull)
                .toList();

        return updateBookGenreIds(book.getId(), genreIds)
                .then(Mono.just(book));
    }

    private Mono<Void> updateBookGenreIds(Long bookId, List<Long> genreIds) {
        return deleteBookGenres(bookId)
                .then(saveBookGenreIds(bookId, genreIds));
    }

    private Mono<Void> deleteBookGenres(Long bookId) {
        return entityTemplate.getDatabaseClient()
                .sql("DELETE FROM books_genres WHERE book_id = :bookId")
                .bind("bookId", bookId)
                .fetch()
                .rowsUpdated()
                .then();
    }

    private Mono<Void> saveBookGenreIds(Long bookId, List<Long> genreIds) {
        if (genreIds == null || genreIds.isEmpty()) {
            return Mono.empty();
        }
        // Проверяем существование жанров
        return genreRepository.findAllById(genreIds).collectList()
                .flatMap(existingGenres -> {
                    List<Long> existingGenreIds = existingGenres.stream()
                            .map(Genre::getId).toList();

                    List<Long> notFoundIds = genreIds.stream()
                            .filter(id -> !existingGenreIds.contains(id)).toList();

                    if (!notFoundIds.isEmpty()) {
                        return Mono.error(new IllegalArgumentException("Genres not found: " + notFoundIds));
                    }

                    // Сохраняем связи
                    return saveLinkBookWithGenres(bookId, existingGenreIds);
                });
    }

    private Mono<Void> saveLinkBookWithGenres(Long bookId, List<Long> existingGenreIds) {
        return Flux.fromIterable(existingGenreIds)
                .flatMap(genreId -> entityTemplate.getDatabaseClient()
                        .sql("INSERT INTO books_genres (book_id, genre_id) VALUES (:bookId, :genreId)")
                        .bind("bookId", bookId)
                        .bind("genreId", genreId)
                        .fetch()
                        .rowsUpdated()
                )
                .then();
    }

    private record BookWithGenre(
            Long bookId,
            String title,
            Author author,
            Genre genre
    ) {
    }
}

