package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.CommentaryDto;
import ru.otus.hw.dto.CommentaryWithBookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Commentary;
import ru.otus.hw.projections.CommentaryProjection;
import ru.otus.hw.repositories.CommentaryRepository;
import ru.otus.hw.repositories.validators.CommentaryValidator;

@Service
@RequiredArgsConstructor
public class CommentaryServiceImpl implements CommentaryService {

    private final CommentaryRepository commentaryRepository;

    private final BookService bookService;

    private final CommentaryValidator validator;

    @Override
    public Flux<CommentaryDto> findAllByBookId(String bookId) {
        return commentaryRepository.findAllByBook(bookId)
                .map(proj -> new Commentary(proj.getId(), proj.getText(), null))
                .sort((o1, o2) -> o1.getId().compareTo(o2.getId()))
                .map(CommentaryDto::fromDomainObject);

    }

    @Override
    public Mono<CommentaryWithBookDto> findByIdWithBook(String id) {
        return commentaryRepository.findById(id)
                .flatMap(this::convertToCommentWithBook)
                .map(CommentaryWithBookDto::fromDomainObject)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Comment not found")));
    }

    @Override
    public Mono<CommentaryWithBookDto> saveComment(CommentaryWithBookDto commentaryDto) {
        if (commentaryDto.getId().isBlank()) {
            commentaryDto.setId(null);
        }

        Commentary commentary = commentaryDto.toDomainObject();
        CommentaryProjection proj = convertToProjection(commentary);

        return validator.validate(proj)
                .then(commentaryRepository.save(proj))
                .flatMap(savedComment -> commentaryRepository.findById(savedComment.getId()))
                .flatMap(this::convertToCommentWithBook)
                .map(CommentaryWithBookDto::fromDomainObject);
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
