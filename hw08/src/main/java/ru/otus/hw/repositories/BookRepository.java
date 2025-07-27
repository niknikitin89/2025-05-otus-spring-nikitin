package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends MongoRepository<Book, String> {

    Optional<Book> findById(@Nonnull String id);

    @Nonnull
    @Override
    List<Book> findAll();

    @Query("{ '_id' : :#{#id}}")
    Optional<Book> findByIdSmall(@Param("id") String id);

}
