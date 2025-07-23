package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;

public interface GenreRepository extends CrudRepository<Genre, Integer> {

    @Nonnull
    List<Genre> findAll();

    @Nonnull
    List<Genre> findAllByIdIn(@Nonnull Set<Long> id);

}
