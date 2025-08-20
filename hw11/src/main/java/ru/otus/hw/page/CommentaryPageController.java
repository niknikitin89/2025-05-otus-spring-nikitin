package ru.otus.hw.page;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class CommentaryPageController {

    //http://localhost:8080/comment/add_to_book/1
    @GetMapping("/comment/add_to_book/{id}")
    public String enterCommentToBook(@PathVariable("id") long bookId) {
        return "enterCommentToBookPage";
    }

    //http://localhost:8080/comment/edit/1
    @GetMapping("/comment/edit/{id}")
    public String editComment(@PathVariable("id") long commentId) {
        return "enterCommentToBookPage";
    }
}
