package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.Author;

import java.util.List;

public interface AuthorRepository extends MongoRepository<Author, String> {
    @Override
    @Nonnull
    List<Author> findAll();

}
