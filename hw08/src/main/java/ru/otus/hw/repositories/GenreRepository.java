package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;

public interface GenreRepository extends MongoRepository<Genre, String> {

    @Nonnull
    @Override
    List<Genre> findAll();

    @Nonnull
    List<Genre> findAllByIdIn(@Nonnull Set<String> id);

}
