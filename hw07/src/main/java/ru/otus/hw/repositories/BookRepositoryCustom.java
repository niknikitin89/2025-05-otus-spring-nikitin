package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.hw.models.Book;

import java.util.Optional;

public interface BookRepositoryCustom {

    @Query("select b from Book b where b.id = :id")
    Optional<Book> findByIdSmall(@Param("id") long id);
}
