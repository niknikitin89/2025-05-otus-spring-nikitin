package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends CrudRepository<Book, Integer>, BookRepositoryCustom {

    @EntityGraph(attributePaths = {"author", "genres"})
    Optional<Book> findById(long id);

    @Nonnull
    @EntityGraph(attributePaths = {"author"})
    List<Book> findAll();

    @Nonnull
    Book save(@Nonnull Book book);

    void deleteById(long id);
}
