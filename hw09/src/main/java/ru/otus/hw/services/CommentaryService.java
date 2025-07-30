package ru.otus.hw.services;

import ru.otus.hw.dto.CommentaryDto;

import java.util.List;

public interface CommentaryService {
    List<CommentaryDto> findByBookId(long id);

    CommentaryDto findById(long id);

    CommentaryDto add(long bookId, String text);

    void deleteById(long id);

    void update(long id, String text);

}
