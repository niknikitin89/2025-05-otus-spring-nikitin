package ru.otus.hw.repositories.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.projections.BookProjection;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

@Component
@RequiredArgsConstructor
public class BookValidatorImpl implements BookValidator {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    @Override
    public Mono<Void> validateBook(BookProjection book) {
        return Mono.defer(() -> validateAuthor(book)
                .then(validateGenres(book)));
    }

    private Mono<Void> validateGenres(BookProjection book) {
        if (isEmpty(book.getGenres())) {
            return Mono.error(new IllegalArgumentException("Genres ids must not be null"));
        }

        List<String> genresIds = book.getGenres();

        return genreRepository.findAllByIdIn(genresIds)
                .map(Genre::getId)
                .collectList()
                .flatMap(existingIds -> {
                    if (existingIds.size() != genresIds.size()) {
                        return Mono.error(
                                new EntityNotFoundException("One or all genres with ids %s not found"
                                        .formatted(genresIds)));
                    }
                    return Mono.empty();
                });

    }

    private Mono<Void> validateAuthor(BookProjection book) {
        var authorId = book.getAuthor();
        if (authorId == null) {
            return Mono.error(new IllegalArgumentException("Author id must not be null"));
        }

        return authorRepository.existsById(authorId)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new IllegalArgumentException("Author with id " + authorId + " not found"));
                    }
                    return Mono.empty();
                });
    }
}
