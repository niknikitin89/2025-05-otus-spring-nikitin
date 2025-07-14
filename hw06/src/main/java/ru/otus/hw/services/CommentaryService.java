package ru.otus.hw.services;

import ru.otus.hw.models.Commentary;

import java.util.List;

public interface CommentaryService {
    List<Commentary> findByBookId(long id);

    Commentary add(long bookId, String text);

    void deleteById(long id);

    void update(long id, String text);
}
