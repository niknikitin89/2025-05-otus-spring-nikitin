package ru.otus.hw.dto;

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

    private String text;

    private BookDto book;

    public static CommentaryDto fromDomainObject(Commentary commet) {
        var book = BookDto.fromDomainObject(commet.getBook());
        return new CommentaryDto(commet.getId(), commet.getText(), book);
    }

    public Commentary toDomainObject() {
        return new Commentary(id, text, book.toDomainObject());
    }

}
