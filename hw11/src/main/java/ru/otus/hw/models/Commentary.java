package ru.otus.hw.models;


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
public class Commentary {

    @EqualsAndHashCode.Include
    @ToString.Include
    private String id;

    @EqualsAndHashCode.Include
    @ToString.Include
    private String text;

    private Book book;

}
