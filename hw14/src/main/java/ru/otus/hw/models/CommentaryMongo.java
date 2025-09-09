package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "commentaries")
public class CommentaryMongo {
    @Id
    @EqualsAndHashCode.Include
    @ToString.Include
    private String id;

    @Field(name = "text")
    @EqualsAndHashCode.Include
    @ToString.Include
    private String text;

    @Field(name = "book")
    private String book;

//    public CommentaryMongo(String text, Book book) {
//        this.text = text;
//        this.book = book;
//    }
}
