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
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.repositories.CommentaryRepository;
import ru.otus.hw.services.CommentaryService;

@RestController
@RequiredArgsConstructor
public class CommentaryController {

    private final CommentaryService commentaryService;
    private final CommentaryRepository commentaryRepository;

    @GetMapping("/api/v1/books/{bookId}/comments")
    public Flux<CommentaryDto> getCommentsForBook(@PathVariable("bookId") long bookId) {
        return commentaryRepository.findAllByBookId(bookId)
                .map(CommentaryDto::fromDomainObject);
    }

    @GetMapping("/api/v1/comments/{id}")
    public Mono<CommentaryDto> getCommentWithBook(@PathVariable("id") long id) {
        return commentaryRepository.findByIdWithBook(id)
                .map(CommentaryDto::fromDomainObjectWithBook)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Comment not found")));
    }

    @PostMapping("/api/v1/comments")
    public Mono<CommentaryDto> addComment(@RequestBody CommentaryDto comment) {
        return commentaryRepository.save(comment.toNewDomainObject())
                .map(CommentaryDto::fromDomainObjectWithBook);
    }

    @PutMapping("/api/v1/comments/{id}")
    public Mono<CommentaryDto> updateComment(@PathVariable("id") long id, @RequestBody CommentaryDto comment) {
        comment.setId(id);
        return commentaryRepository.save(comment.toDomainObject())
                .map(CommentaryDto::fromDomainObjectWithBook);
    }

    @DeleteMapping("/api/v1/comments/{id}")
    public Mono<Void> deleteComment(@PathVariable("id") long id) {
        return commentaryRepository.deleteById(id);
    }
}
