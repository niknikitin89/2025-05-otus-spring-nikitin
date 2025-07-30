package ru.otus.hw.dto;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class BookDto {

    private long id;
    private String title;
    @NotBlank(message = "Empty author")
    private Author author;
    @NotBlank(message = "Empty author")
    private List<Genre> genres;

    public static BookDto fromDomainObject(@Nonnull Book book) {
        return new BookDto(book.getId(), book.getTitle(), book.getAuthor(), book.getGenres().stream().toList());
    }

    public Book toDomainObject() {
        return new Book(id, title, author, genres);
    }

    public BookDto(){
        genres = new ArrayList<>();
    }
}
