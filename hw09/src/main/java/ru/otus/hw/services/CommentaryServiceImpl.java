package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.CommentaryDto;
import ru.otus.hw.models.Commentary;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentaryRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentaryServiceImpl implements CommentaryService {

    private final CommentaryRepository commentaryRepository;

    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CommentaryDto> findByBookId(long id) {

        return commentaryRepository.findAllByBookId(id)
                .stream().map(CommentaryDto::fromDomainObject).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CommentaryDto findById(long id) {
        var comment = commentaryRepository.findById(id);
        return comment.map(CommentaryDto::fromDomainObject).orElse(null);
    }

    @Override
    @Transactional
    public CommentaryDto add(long bookId, String text) {
        if (bookId == 0) {
            throw new IllegalArgumentException("Book id cannot be 0");
        }

        var book = bookRepository.findByIdSmall(bookId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Book %d not found".formatted(bookId)));

        Commentary commentary = new Commentary(0, text, book);
        return CommentaryDto.fromDomainObject(commentaryRepository.save(commentary));
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        commentaryRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void update(long id, String text) {
        if (id == 0) {
            throw new IllegalArgumentException("Commentary id cannot be 0");
        }

        var commentary = commentaryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Commentary %d not found".formatted(id)));

        commentary.setText(text);
        commentaryRepository.save(commentary);
    }
}
