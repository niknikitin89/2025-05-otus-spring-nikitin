package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Commentary;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentaryRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentaryServiceImpl implements CommentaryService {

    private final CommentaryRepository commentaryRepository;

    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Commentary> findByBookId(long id) {
        return commentaryRepository.findAllByBookId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Commentary> findById(long id) {
        return commentaryRepository.findById(id);
    }

    @Override
    @Transactional
    public Commentary add(long bookId, String text) {
        if (bookId == 0) {
            throw new IllegalArgumentException("Book id cannot be 0");
        }

        var book = bookRepository.findByIdSmall(bookId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Book %d not found".formatted(bookId)));

        Commentary commentary = new Commentary(0, text, book);
        return commentaryRepository.save(commentary);
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
