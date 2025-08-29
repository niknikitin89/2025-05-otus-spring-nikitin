package ru.otus.hw.dto;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.Book;

@Data
@AllArgsConstructor
public class BookForCommentDto {

    private String id;

    private String title;

    public static BookForCommentDto fromDomainObject(@Nonnull Book book) {
        return new BookForCommentDto(book.getId(), book.getTitle());
    }

    public Book toDomainObject() {
        return new Book(id, title,null, null);
    }
}
