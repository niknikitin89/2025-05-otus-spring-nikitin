package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Commentary;
import ru.otus.hw.projections.CommentaryProjection;
import ru.otus.hw.repositories.CommentaryRepository;

@Service
@RequiredArgsConstructor
public class CommentaryServiceImpl implements CommentaryService {

    private final CommentaryRepository commentaryRepository;

    private final BookService bookService;

    @Override
    public Flux<Commentary> findAllByBookId(String bookId) {
        return commentaryRepository.findAllByBook(bookId)
                .map(proj -> new Commentary(proj.getId(), proj.getText(), null));

    }

    @Override
    public Mono<Commentary> findByIdWithBook(String id) {
        return commentaryRepository.findById(id)
                .flatMap(this::convertToCommentWithBook);
    }

    @Override
    public Mono<Commentary> saveComment(Commentary commentary) {
        return commentaryRepository.save(convertToProjection(commentary))
                .flatMap(this::convertToCommentWithBook);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return commentaryRepository.deleteById(id);
    }

    private CommentaryProjection convertToProjection(Commentary commentary) {
        return new CommentaryProjection(commentary.getId(), commentary.getText(), commentary.getBook().getId());
    }

    private Mono<Commentary> convertToCommentWithBook(CommentaryProjection commentaryProjection) {

        return bookService.findById(commentaryProjection.getBook())
                .map(book -> new Commentary(
                        commentaryProjection.getId(),
                        commentaryProjection.getText(),
                        book)
                );
    }

}
