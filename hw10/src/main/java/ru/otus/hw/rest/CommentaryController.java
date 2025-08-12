package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.CommentaryDto;
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

    @DeleteMapping("/api/comment/{id}")
    public void deleteComment(@PathVariable("id") long id) {
        commentaryService.deleteById(id);
    }
}
