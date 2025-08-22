package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import ru.otus.hw.models.Genre;

import java.util.List;

public interface GenreRepository extends ReactiveMongoRepository<Genre, String> {

    @Nonnull
    @Override
    Flux<Genre> findAll();

    //    @Query("""
//        SELECT g.* FROM genres g
//        JOIN books_genres bg ON g.id = bg.genre_id
//        WHERE bg.book_id = :bookId
//        """)
//    @Nonnull
//    Flux<Genre> findAllByBook(@Nonnull String id);

    //
//    @Query("""
//            SELECT g.* FROM genres g
//            where g.id in (:genreId)
//            """)
    Flux<Genre> findAllByIdIn(@Nonnull List<String> ids);

}
