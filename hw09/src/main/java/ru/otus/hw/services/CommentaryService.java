package ru.otus.hw.services;

import ru.otus.hw.dto.CommentaryDto;

import java.util.List;
import java.util.Optional;

public interface CommentaryService {
    List<CommentaryDto> findByBookId(long id);

    Optional<CommentaryDto> findById(long id);

    CommentaryDto add(long bookId, String text);

    void deleteById(long id);

    void update(long id, String text);

    Optional<CommentaryDto> findByIdWithBook(long id);
}
