package ru.otus.hw.rest;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.CommentaryDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.CommentaryService;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentaryController {

    private final CommentaryService commentaryService;

    @GetMapping("/api/v1/books/{bookId}/comments")
    @CircuitBreaker(name = "default-service", fallbackMethod = "getCommentsForBookFallback")
    @Retry(name = "default-service")
    public List<CommentaryDto> getCommentsForBook(@PathVariable("bookId") long bookId) {

        return commentaryService.findByBookId(bookId);
    }

    @GetMapping("/api/v1/comments/{id}")
    @CircuitBreaker(name = "default-service", fallbackMethod = "getCommentWithBookFallback")
    @Retry(name = "default-service")
    public CommentaryDto getCommentWithBook(@PathVariable("id") long id) {

        return commentaryService.findByIdWithBook(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Comment not found"));
    }

    @PostMapping("/api/v1/comments")
    @CircuitBreaker(name = "default-service", fallbackMethod = "writeOperationFallback")
    @Retry(name = "default-service")
    public CommentaryDto addComment(@RequestBody CommentaryDto comment) {

        return commentaryService.add(comment.getBook().getId(), comment.getText());
    }

    @PutMapping("/api/v1/comments/{id}")
    @CircuitBreaker(name = "default-service")
    @Retry(name = "default-service")
    public void updateComment(@PathVariable("id") long id, @RequestBody CommentaryDto comment) {

        comment.setId(id);
        commentaryService.update(comment.getId(), comment.getText());
    }

    @DeleteMapping("/api/v1/comments/{id}")
    @CircuitBreaker(name = "default-service")
    @Retry(name = "default-service")
    public void deleteComment(@PathVariable("id") long id) {

        commentaryService.deleteById(id);
    }


    // FALLBACK МЕТОДЫ

    // Общий fallback для операций чтения
    public CommentaryDto getCommentWithBookFallback(long id, Exception e) {

        System.err.println("Read operation fallback. Parameter: " + id + ", Error: " + e.getMessage());
        throw new EntityNotFoundException("Service unavailable. Unable to retrieve data for ID: " + id);
    }

    public List<CommentaryDto> getCommentsForBookFallback(long id, Exception e) {

        return Collections.emptyList();
    }

    // Общий fallback для операций записи
    public CommentaryDto writeOperationFallback(CommentaryDto parameter, Exception e) {

        throw new RuntimeException("Write operation temporarily unavailable. Please try again later.");
    }
}
