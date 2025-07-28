package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.Commentary;

import java.util.List;

public interface CommentaryRepository extends MongoRepository<Commentary, String> {

    @Nonnull
    List<Commentary> findAllByBookId(String id);

    void deleteAllByBookId(String id);

}
