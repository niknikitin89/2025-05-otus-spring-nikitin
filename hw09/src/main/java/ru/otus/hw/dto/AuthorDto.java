package ru.otus.hw.dto;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.Author;

@Data
@AllArgsConstructor
public class AuthorDto {

    private long id;

    private String fullName;

    public static AuthorDto fromDomainObject(@Nonnull Author author) {
        return new AuthorDto(author.getId(), author.getFullName());
    }

    public Author toDomainObject(){
        return new Author(id, fullName);
    }

}
