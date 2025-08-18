package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CustomBookRepositoryImpl implements CustomBookRepository {

    private final R2dbcEntityTemplate entityTemplate;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    @Override
    public Flux<Book> findAllWithAuthorsAndGenres() {
        return entityTemplate.getDatabaseClient()
                .sql("""
                        SELECT b.id as book_id, b.title, 
                               a.id as author_id, a.full_name,
                               g.id as genre_id, g.name as genre_name
                        FROM books b
                        JOIN authors a ON b.author_id = a.id
                        LEFT JOIN books_genres bg ON b.id = bg.book_id
                        LEFT JOIN genres g ON bg.genre_id = g.id
                        """)
                .map((row, meta) -> {
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
                })
                .all()
                .groupBy(BookWithGenre::bookId)
                .flatMap(group -> group.collectList()
                        .map(items -> {
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
                        })
                );
    }

    private record BookWithGenre(
            Long bookId,
            String title,
            Author author,
            Genre genre
    ) {
    }
}