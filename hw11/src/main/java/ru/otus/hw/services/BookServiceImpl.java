package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.projections.BookProjection;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final ReactiveMongoTemplate mongoTemplate;

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    @Override
    public Flux<Book> findAllFullBooks() {
        return bookRepository.findAll()
                .collectList()
                .flatMapMany(bookProjections -> convertToFullBooks(bookProjections));

    }

    private Flux<Book> convertToFullBooks(List<BookProjection> bookProjections) {
        // Собираем все ID авторов и жанров
        Set<String> allAuthorIds = bookProjections.stream()
                .map(BookProjection::getAuthor)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<String> allGenreIds = bookProjections.stream()
                .flatMap(proj -> proj.getGenres().stream())
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // Загружаем всех авторов и жанры одним запросом
        Mono<Map<String, Author>> authorsMapMono = authorRepository.findAllById(allAuthorIds)
                .collectMap(Author::getId, Function.identity());

        Mono<Map<String, Genre>> genresMapMono = genreRepository.findAllById(allGenreIds)
                .collectMap(Genre::getId, Function.identity());

        // Преобразуем проекции в полные книги
        return Mono.zip(authorsMapMono, genresMapMono)
                .flatMapMany(tuple -> {
                    Map<String, Author> authorsMap = tuple.getT1();
                    Map<String, Genre> genresMap = tuple.getT2();

                    return Flux.fromIterable(bookProjections)
                            .map(projection -> createFullBook(projection, authorsMap, genresMap));
                });
    }

    private Book createFullBook(BookProjection projection,
                                Map<String, Author> authorsMap,
                                Map<String, Genre> genresMap) {

        Book book = new Book();
        book.setId(projection.getId());
        book.setTitle(projection.getTitle());

        // Устанавливаем автора
        if (projection.getAuthor() != null) {
            book.setAuthor(authorsMap.get(projection.getAuthor()));
        }

        // Устанавливаем жанры
        if (projection.getGenres() != null) {
            List<Genre> genres = projection.getGenres().stream()
                    .map(genresMap::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            book.setGenres(genres);
        }

        return book;
    }
}
