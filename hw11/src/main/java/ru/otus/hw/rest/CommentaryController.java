package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.CommentaryDto;
import ru.otus.hw.dto.CommentaryWithBookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.repositories.CommentaryRepository;
//import ru.otus.hw.services.CommentaryService;

@RestController
@RequiredArgsConstructor
public class CommentaryController {

    private final CommentaryRepository commentaryRepository;

    @GetMapping("/api/v1/books/{bookId}/comments")
    public Flux<CommentaryDto> getCommentsForBook(@PathVariable("bookId") long bookId) {
        return commentaryRepository.findAllByBookId(bookId)
                .map(CommentaryDto::fromDomainObject);
    }

    @GetMapping("/api/v1/comments/{id}")
    public Mono<CommentaryWithBookDto> getCommentWithBook(@PathVariable("id") long id) {
        return commentaryRepository.findByIdWithBook(id)
                .map(CommentaryWithBookDto::fromDomainObject)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Comment not found")));
    }

    @PostMapping("/api/v1/comments")
    public Mono<CommentaryWithBookDto> addComment(@RequestBody CommentaryWithBookDto comment) {
        return Mono.defer(() -> {
            if (comment.getText().isEmpty()) {
                return Mono.error(new IllegalArgumentException("Commentary is empty"));
            }
            if (comment.getBook().getId() == null || comment.getBook().getId() == 0) {
                return Mono.error(new IllegalArgumentException("Book id cannot be 0"));
            }

            return commentaryRepository.save(comment.toDomainObject())
                    .map(CommentaryWithBookDto::fromDomainObject);
        });
    }

    @PutMapping("/api/v1/comments/{id}")
    public Mono<CommentaryWithBookDto> updateComment(
            @PathVariable("id") long id,
            @RequestBody CommentaryWithBookDto comment) {

        if (id == 0) {
            return Mono.error(new IllegalArgumentException("Commentary id cannot be 0"));
        }

        comment.setId(id);
        return commentaryRepository
                .existsById(id)
                .flatMap(commExist -> {
                    if (!commExist) {
                        return Mono.error(new IllegalArgumentException(
                                "Commentary %d not found".formatted(id)));
                    }
                    return commentaryRepository
                            .save(comment.toDomainObject())
                            .map(CommentaryWithBookDto::fromDomainObject);
                });
    }

    @DeleteMapping("/api/v1/comments/{id}")
    public Mono<Void> deleteComment(@PathVariable("id") long id) {
        return commentaryRepository.deleteById(id);
    }
}
