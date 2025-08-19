package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.models.Commentary;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentaryDto {

    private long id;

    @NotBlank(message = "Empty Text")
    private String text;

    private Long bookId;

    private BookDto book;

    public static CommentaryDto fromDomainObjectWithBook(Commentary commet) {
        var book = BookDto.fromDomainObjectOnlyBook(commet.getBook());
        return new CommentaryDto(commet.getId(), commet.getText(), commet.getBookId(), book);
    }

    public static CommentaryDto fromDomainObject(Commentary commet) {
        return new CommentaryDto(commet.getId(), commet.getText(), commet.getBookId(), null);
    }

    public Commentary toDomainObject() {
        return new Commentary(id, text, book.getId(), book.toDomainObjectOnlyBook());
    }

    public Commentary toNewDomainObject() {
        return new Commentary(null, text, book.getId(), book.toDomainObjectOnlyBook());
    }

}
