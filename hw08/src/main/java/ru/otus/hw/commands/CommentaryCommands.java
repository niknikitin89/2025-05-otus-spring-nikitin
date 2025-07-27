package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.CommentaryConverter;
import ru.otus.hw.services.CommentaryService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class CommentaryCommands {

    private final CommentaryService commentaryService;

    private final CommentaryConverter commentaryConverter;

    // cbid 3
    @ShellMethod(value = "Find comment by id", key = "cbid")
    public String findById(String id) {
        var result = commentaryService.findById(id);
        if (result.isPresent()) {
            return commentaryConverter.commentaryToString(result.get());
        } else {
            return "Comment not found";
        }
    }

    // cb 3
    @ShellMethod(value = "Find comment by book", key = "cb")
    public String findByBookId(String bookId) {

        StringBuilder sb = new StringBuilder("Commentaries of book %s: %n".formatted(bookId));
        sb.append(commentaryService.findByBookId(bookId).stream()
                .map(commentaryConverter::commentaryToString)
                .collect(Collectors.joining(System.lineSeparator())));

        return sb.toString();
    }

    // ca 3 "New Comment"
    @ShellMethod(value = "Add commentary to Book", key = "ca")
    public String addToBook(String bookId, String text) {
        commentaryService.add(bookId, text);
        return findByBookId(bookId);
    }

    // cd 4
    @ShellMethod(value = "Delete commentary by Id", key = "cd")
    public void deleteById(String id) {
        commentaryService.deleteById(id);
    }

    // cu 5 "Updated text"
    @ShellMethod(value = "Update commentary by Id", key = "cu")
    public void updateById(String id, String text) {
        commentaryService.update(id, text);
    }

}
