package ru.otus.hw.repositories;

import ru.otus.hw.models.Commentary;

import java.util.List;
import java.util.Optional;

public interface CommentaryRepository {

    Optional<Commentary> findById(long id);

    List<Commentary> findAllByBookId(long id);

    Commentary save(Commentary commentary);

    void deleteById(long id);
}
