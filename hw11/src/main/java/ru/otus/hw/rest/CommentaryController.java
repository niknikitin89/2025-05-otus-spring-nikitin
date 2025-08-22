package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.CommentaryDto;
import ru.otus.hw.dto.CommentaryWithBookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.CommentaryService;
//import ru.otus.hw.services.CommentaryService;

@RestController
@RequiredArgsConstructor
public class CommentaryController {

    private final CommentaryService commentaryService;

    @GetMapping("/api/v1/books/{bookId}/comments")
    public Flux<CommentaryDto> getCommentsForBook(@PathVariable("bookId") String bookId) {
        return commentaryService.findAllByBookId(bookId)
                .sort((o1, o2) -> o1.getId().compareTo(o2.getId()))
                .map(CommentaryDto::fromDomainObject);
    }

    @GetMapping("/api/v1/comments/{id}")
    public Mono<CommentaryWithBookDto> getCommentWithBook(@PathVariable("id") String  id) {
        return commentaryService.findByIdWithBook(id)
                .map(CommentaryWithBookDto::fromDomainObject)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Comment not found")));
    }

//    @PostMapping("/api/v1/comments")
//    public Mono<CommentaryWithBookDto> addComment(@RequestBody CommentaryWithBookDto comment) {
//        return Mono.defer(() -> {
//            if (comment.getText().isEmpty()) {
//                return Mono.error(new IllegalArgumentException("Commentary is empty"));
//            }
//            if (comment.getBook().getId() == null || comment.getBook().getId().isEmpty()) {
//                return Mono.error(new IllegalArgumentException("Book id cannot be empty"));
//            }
//
//            return commentaryRepository.save(comment.toDomainObject())
//                    .map(CommentaryWithBookDto::fromDomainObject);
//        });
//    }
//
    @PutMapping("/api/v1/comments/{id}")
    public Mono<CommentaryWithBookDto> updateComment(
            @PathVariable("id") String id,
            @RequestBody CommentaryWithBookDto comment) {

        if (id == null || id.isBlank()) {
            return Mono.error(new IllegalArgumentException("Commentary id cannot be empty"));
        }

        comment.setId(id);
        return commentaryService.saveComment(comment.toDomainObject())
                .map(CommentaryWithBookDto::fromDomainObject);


//
//        comment.setId(id);
//        return commentaryRepository
//                .existsById(id)
//                .flatMap(commExist -> {
//                    if (!commExist) {
//                        return Mono.error(new IllegalArgumentException(
//                                "Commentary %d not found".formatted(id)));
//                    }
//                    return commentaryRepository
//                            .save(comment.toDomainObject())
//                            .map(CommentaryWithBookDto::fromDomainObject);
//                });
    }

    @DeleteMapping("/api/v1/comments/{id}")
    public Mono<Void> deleteComment(@PathVariable("id") String id) {
        return commentaryService.deleteById(id);
    }
}
