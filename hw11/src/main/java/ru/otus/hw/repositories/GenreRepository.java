package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import ru.otus.hw.models.Genre;

import java.util.List;

public interface GenreRepository extends ReactiveCrudRepository<Genre, Long> {

    @Nonnull
    @Override
    Flux<Genre> findAll();

    @Query("""
        SELECT g.* FROM genres g
        JOIN books_genres bg ON g.id = bg.genre_id
        WHERE bg.book_id = :bookId
        """)
    @Nonnull
    Flux<Genre> findByBookId(@Nonnull Long id);

    @Query("""
            SELECT g.* FROM genres g
            where g.id in (:genreId)
            """)
    Flux<Genre> findAllByIdIn(@Nonnull List<Long> ids);

}
