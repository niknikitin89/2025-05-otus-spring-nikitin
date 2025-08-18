package ru.otus.hw.models;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "commentaries")
public class Commentary {
    @Id
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @Column("text")
    @EqualsAndHashCode.Include
    @ToString.Include
    private String text;

    @Column("book_id")
    private Book book;
}
