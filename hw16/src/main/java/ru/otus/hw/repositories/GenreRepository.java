package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;

@RepositoryRestResource(path = "genres")
public interface GenreRepository extends CrudRepository<Genre, Long> {

    @Nonnull
    @Override
    List<Genre> findAll();

    @Nonnull
    List<Genre> findAllByIdIn(@Nonnull Set<Long> id);

}
