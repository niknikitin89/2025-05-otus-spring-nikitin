package ru.otus.hw.services;

import ru.otus.hw.models.Commentary;

import java.util.List;
import java.util.Optional;

public interface CommentaryService {
    List<Commentary> findByBookId(String id);

    Optional<Commentary> findById(String id);

    Commentary add(String bookId, String text);

    void deleteById(String id);

    void update(String id, String text);
}
