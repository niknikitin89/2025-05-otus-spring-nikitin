package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends CrudRepository<Author, Long> {
    @Override
    @Nonnull
    List<Author> findAll();

    Optional<Author> findById(long id);
}
