package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.models.Commentary;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentaryWithBookDto {

    private Long id;

    @NotBlank(message = "Empty Text")
    private String text;

    private BookForCommentDto book;

    public static CommentaryWithBookDto fromDomainObject(Commentary commet) {
        return new CommentaryWithBookDto(commet.getId(), commet.getText(),
                BookForCommentDto.fromDomainObject(commet.getBook()));
    }

    public Commentary toDomainObject() {
        return new Commentary(id, text, book.getId(), book.toDomainObject());
    }

}
