package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Commentary;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentaryDto {

    private long id;

    @NotBlank(message = "Empty Text")
    private String text;

    private BookDto book;

    public static CommentaryDto fromDomainObjectWithBook(Commentary commet) {
        var book = BookDto.fromDomainObject(commet.getBook());
        return new CommentaryDto(commet.getId(), commet.getText(), book);
    }

    public static CommentaryDto fromDomainObject(Commentary commet) {
        return new CommentaryDto(commet.getId(), commet.getText(), null);
    }

    public Commentary toDomainObject() {
        return new Commentary(id, text, book.toDomainObject());
    }

}
