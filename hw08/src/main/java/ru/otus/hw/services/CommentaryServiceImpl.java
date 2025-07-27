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
    public List<Commentary> findByBookId(String id) {
        return commentaryRepository.findAllByBookId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Commentary> findById(String id) {
        return commentaryRepository.findById(id);
    }

    @Override
    @Transactional
    public Commentary add(String bookId, String text) {
        if (bookId.equals("0") || bookId.isEmpty()) {
            throw new IllegalArgumentException("Book id cannot be empty");
        }

        var book = bookRepository.findByIdSmall(bookId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Book %s not found".formatted(bookId)));

        Commentary commentary = new Commentary(text, book);
        return commentaryRepository.save(commentary);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        commentaryRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void update(String id, String text) {
        if (id.equals("0")||id.isEmpty()) {
            throw new IllegalArgumentException("Commentary id cannot be empty");
        }

        var commentary = commentaryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Commentary %s not found".formatted(id)));

        commentary.setText(text);
        commentaryRepository.save(commentary);
    }
}
