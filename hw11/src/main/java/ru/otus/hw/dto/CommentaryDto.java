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

    private String id;

    @NotBlank(message = "Empty Text")
    private String text;

    private String bookId;

    public static CommentaryDto fromDomainObject(Commentary commet) {
        return new CommentaryDto(commet.getId(), commet.getText(), commet.getBook().getId());
    }

    public Commentary toDomainObject() {
        return new Commentary(id, text, null );
    }

}
