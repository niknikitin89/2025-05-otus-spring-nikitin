package ru.otus.hw.repositories.validators;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.otus.hw.projections.CommentaryProjection;
import ru.otus.hw.repositories.CommentaryRepository;

@Component
public class CommentaryValidatorImpl implements CommentaryValidator {
    private final CommentaryRepository commentaryRepository;

    public CommentaryValidatorImpl(CommentaryRepository commentaryRepository) {
        this.commentaryRepository = commentaryRepository;
    }

    @Override
    public Mono<Void> validate(CommentaryProjection comment) {
        return validateText(comment.getText())
                .then(validateBook(comment.getBook()))
                .then(validateExistence(comment.getId()));
    }

    private Mono<Void> validateText(String text) {
        if (text == null || text.isBlank()) {
            return Mono.error(new IllegalArgumentException("Comment text is empty"));
        }
        return Mono.empty();
    }

    private Mono<Void> validateBook(String bookId) {
        if (bookId == null || bookId.isBlank()) {
            return Mono.error(new IllegalArgumentException("Book ID is empty"));
        }
        return Mono.empty();
    }

    private Mono<Void> validateExistence(String commentId) {
        if (commentId != null && !commentId.isBlank()) {
            return commentaryRepository.existsById(commentId)
                    .flatMap(exists -> {
                        if (!exists) {
                            return Mono.error(new IllegalArgumentException(
                                    "Commentary %s not found".formatted(commentId)
                            ));
                        }
                        return Mono.empty();
                    });
        }
        return Mono.empty();
    }
}
