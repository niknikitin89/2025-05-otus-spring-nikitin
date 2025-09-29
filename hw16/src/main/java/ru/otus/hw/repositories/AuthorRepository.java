package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.hw.models.Author;

import java.util.List;

@RepositoryRestResource(path = "authors")
public interface AuthorRepository extends CrudRepository<Author, Long> {
    @Override
    @Nonnull
    List<Author> findAll();

}
