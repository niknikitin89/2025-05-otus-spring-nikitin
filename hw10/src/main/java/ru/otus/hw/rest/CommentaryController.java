package ru.otus.hw.rest;

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

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentaryController {

    private final CommentaryService commentaryService;

    @GetMapping("/api/book/{bookId}/comments")
    public List<CommentaryDto> getCommentsForBook(@PathVariable("bookId") long bookId) {
        return commentaryService.findByBookId(bookId);
    }

    @GetMapping("/api/comment/{id}")
    public CommentaryDto getCommentWithBook(@PathVariable("id") long id) {
        return commentaryService.findByIdWithBook(id)
                .orElseThrow(
                () -> new EntityNotFoundException("Comment not found"));
    }

    @PostMapping("/api/comment")
    public CommentaryDto addComment(@RequestBody CommentaryDto comment) {
        return commentaryService.add(comment.getBook().getId(), comment.getText());
    }

    @PutMapping("/api/comment")
    public void updateComment(@RequestBody CommentaryDto comment) {
        commentaryService.update(comment.getId(),comment.getText());
    }

    @DeleteMapping("/api/comment/{id}")
    public void deleteComment(@PathVariable("id") long id) {
        commentaryService.deleteById(id);
    }
}
