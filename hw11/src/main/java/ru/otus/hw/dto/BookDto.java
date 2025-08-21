package ru.otus.hw.dto;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.Book;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class BookDto {

    private String id;

    @NotBlank(message = "Empty Title")
    private String title;

    private AuthorDto author;

    private List<GenreDto> genres;

    public BookDto() {
        genres = new ArrayList<>();
    }

    public static BookDto fromDomainObject(@Nonnull Book book) {
        return new BookDto(book.getId(), book.getTitle(),
                AuthorDto.fromDomainObject(book.getAuthor()),
                book.getGenres().stream().map(GenreDto::fromDomainObject).toList());
    }

    public static BookDto fromDomainObjectOnlyBook(@Nonnull Book book) {
        return new BookDto(book.getId(), book.getTitle(), null, null);
    }


    public Book toDomainObject() {
        return new Book(id, title,
                author.toDomainObject(),
                genres.stream().map(GenreDto::toDomainObject).toList());
    }


    public Book toNewDomainObject() {
        return new Book(null, title,
                author.toDomainObject(),
                genres.stream().map(GenreDto::toDomainObject).toList());
    }

}
