package ru.otus.hw.page;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentaryService;

@Controller
@RequiredArgsConstructor
public class CommentaryPageController {

    private final CommentaryService commentaryService;

    private final BookService bookService;

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
