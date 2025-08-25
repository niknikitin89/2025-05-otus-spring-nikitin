package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookWithAuthorAndGenresDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.projections.BookProjection;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentaryRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.repositories.validators.BookValidator;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final CommentaryRepository commentaryRepository;

    private final BookValidator bookValidator;

    @Override
    public Flux<BookWithAuthorAndGenresDto> findAllFullBooks() {
        return bookRepository.findAll()
                .collectList()
                .flatMapMany(this::convertToFullBooks)
                .map(BookWithAuthorAndGenresDto::fromDomainObject);

    }

    @Override
    public Mono<Book> findById(String id) {
        return bookRepository.findById(id)
                .map(proj -> new Book(proj.getId(), proj.getTitle(), null, null));
    }

    @Override
    public Mono<BookWithAuthorAndGenresDto> findByIdFullBook(String id) {
        return bookRepository.findById(id)
                .flatMap(this::convertToFullBook)
                .map(BookWithAuthorAndGenresDto::fromDomainObject)
                .switchIfEmpty(Mono.error(
                        new EntityNotFoundException("Book with id " + id + " not found")
                ));
    }

    @Override
    public Mono<BookWithAuthorAndGenresDto> saveBook(BookWithAuthorAndGenresDto bookDto) {
        if (bookDto.getId().isBlank()) {
            bookDto.setId(null);
        }

        Book book = bookDto.toDomainObject();
        BookProjection projection = convertToProjection(book);

        return bookValidator.validate(projection)
                .then(bookRepository.save(projection))
                .flatMap(savedBook -> bookRepository.findById(savedBook.getId()))
                .flatMap(this::convertToFullBook)
                .map(BookWithAuthorAndGenresDto::fromDomainObject);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return Mono.when(
                commentaryRepository.deleteAllByBook(id),
                bookRepository.deleteById(id)
        ).then();
    }

    private BookProjection convertToProjection(Book book) {
        return new BookProjection(book.getId(), book.getTitle(), book.getAuthor().getId(),
                book.getGenres().stream().map(Genre::getId).toList());
    }

    private Mono<Book> convertToFullBook(BookProjection bookProjection) {
        Mono<Author> authorMono = authorRepository.findById(bookProjection.getAuthor());

        List<String> allGenreIds = bookProjection.getGenres();
        Mono<List<Genre>> genresListMono = genreRepository.findAllById(allGenreIds).collectList();

        return Mono.zip(authorMono, genresListMono)
                .flatMap(tuple -> {
                    Author author = tuple.getT1();
                    List<Genre> genres = tuple.getT2();

                    Book book = new Book(bookProjection.getId(), bookProjection.getTitle(), author, genres);

                    return Mono.just(book);
                });
    }

    private Flux<Book> convertToFullBooks(List<BookProjection> bookProjections) {
        // Собираем все ID авторов и выгружаем
        Mono<Map<String, Author>> authorsMapMono = getAuthorsMap(bookProjections);

        // Собираем все ID жанров и выгружаем
        Mono<Map<String, Genre>> genresMapMono = getGenresMap(bookProjections);

        // Преобразуем проекции в полные книги
        return Mono.zip(authorsMapMono, genresMapMono)
                .flatMapMany(tuple -> {
                    Map<String, Author> authorsMap = tuple.getT1();
                    Map<String, Genre> genresMap = tuple.getT2();

                    return Flux.fromIterable(bookProjections)
                            .map(projection -> createFullBook(projection, authorsMap, genresMap));
                });
    }

    private Mono<Map<String, Genre>> getGenresMap(List<BookProjection> bookProjections) {
        Set<String> allGenreIds = bookProjections.stream()
                .flatMap(proj -> proj.getGenres().stream())
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        return genreRepository.findAllById(allGenreIds)
                .collectMap(Genre::getId, Function.identity());
    }

    private Mono<Map<String, Author>> getAuthorsMap(List<BookProjection> bookProjections) {
        Set<String> allAuthorIds = bookProjections.stream()
                .map(BookProjection::getAuthor)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        return authorRepository.findAllById(allAuthorIds)
                .collectMap(Author::getId, Function.identity());
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
                    .toList();
            book.setGenres(genres);
        }

        return book;
    }
}
