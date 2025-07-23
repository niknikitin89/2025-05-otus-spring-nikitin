package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.models.Commentary;

import java.util.List;
import java.util.Optional;

public interface CommentaryRepository extends CrudRepository<Commentary, Long> {

    Optional<Commentary> findById(long id);

    @Nonnull
    List<Commentary> findAllByBookId(long id);

    @Nonnull
    Commentary save(@Nonnull Commentary commentary);

    void deleteById(long id);
}
