package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.hw.models.Commentary;

import java.util.List;

@RepositoryRestResource(path = "commentaries")
public interface CommentaryRepository extends CrudRepository<Commentary, Long> {

    @Nonnull
    List<Commentary> findAllByBookId(long id);

}
