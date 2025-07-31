package ru.otus.hw.controllers;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.dto.CommentaryDto;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentaryService;

@Controller
@RequiredArgsConstructor
public class CommentaryController {

    private final CommentaryService commentaryService;

    private final BookService bookService;

    //http://localhost:8080/comment/add_to_book/1
    @GetMapping("/comment/add_to_book/{id}")
    public String enterCommentToBook(@PathVariable("id") long bookId, Model model) {
        var book = bookService.findById(bookId).orElseThrow(()->new EntityNotFoundException("Book not found"));
        model.addAttribute("book", book);
        CommentaryDto comment = new CommentaryDto();
        model.addAttribute("comment", comment);
        return "enterCommentToBookPage";
    }

    @PostMapping("/comment/save")
    public String saveCommentForBook(@RequestParam("bookId") long bookId,
                                     @RequestParam("commentId") long commentId,
                                     @Valid @RequestParam String text) {
        if (commentId == 0) {
            commentaryService.add(bookId, text);
        } else {
            commentaryService.update(commentId, text);
        }
        return "redirect:/book/" + bookId;
    }

    @PostMapping("/comment/del")
    public String deleteComment(
            @RequestParam long commentId,
            @RequestParam long bookId) {
        commentaryService.deleteById(commentId);
        return "redirect:/book/" + bookId;
    }

    @GetMapping("/comment/edit/{id}")
    public String editComment(@PathVariable("id") long commentId, Model model) {
        var commentOpt = commentaryService.findByIdWithBook(commentId);
        CommentaryDto comment = commentOpt.orElseThrow(
                () -> new EntityNotFoundException("Comment not found"));
        model.addAttribute("book", comment.getBook());
        model.addAttribute("comment", comment);
        return "enterCommentToBookPage";
    }
}
