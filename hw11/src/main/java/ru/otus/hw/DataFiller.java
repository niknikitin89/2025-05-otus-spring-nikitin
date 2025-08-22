package ru.otus.hw;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Commentary;
import ru.otus.hw.models.Genre;
import ru.otus.hw.projections.BookProjection;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentaryRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataFiller implements ApplicationRunner {

    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;

    private final GenreRepository genreRepository;

    private final CommentaryRepository commentaryRepository;

    private final Scheduler workerPool;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        authorRepository.deleteAll().publishOn(workerPool).subscribe();
        genreRepository.deleteAll().publishOn(workerPool).subscribe();
        bookRepository.deleteAll().publishOn(workerPool).subscribe();
        commentaryRepository.deleteAll().publishOn(workerPool).subscribe();


        authorRepository.saveAll(Arrays.asList(
                DataGenerator.getAuthor("1"),
                DataGenerator.getAuthor("2"),
                DataGenerator.getAuthor("3")
        )).publishOn(workerPool).subscribe();

        genreRepository.saveAll(Arrays.asList(
                DataGenerator.getGenre("2"),
                DataGenerator.getGenre("1"),
                DataGenerator.getGenre("3"),
                DataGenerator.getGenre("4"),
                DataGenerator.getGenre("5"),
                DataGenerator.getGenre("6")
        )).publishOn(workerPool).subscribe();


        bookRepository.saveAll(Arrays.asList(
                DataGenerator.getBook("1", "1", List.of("1", "2")),
                DataGenerator.getBook("2", "2", List.of("3", "4")),
                DataGenerator.getBook("3", "3", List.of("5", "6"))
        )).publishOn(workerPool).subscribe();

//        Mono.zip(
//                        bookRepository.findById("1"),
//                        bookRepository.findById("2"),
//                        bookRepository.findById("3")
//                )
//                .switchIfEmpty(Mono.error(new IllegalStateException("No books in database")))
//                .flatMap(tuple -> {
//                    BookProjection book1 = tuple.getT1();
//                    BookProjection book2 = tuple.getT2();
//                    BookProjection book3 = tuple.getT3();
//
//                    return commentaryRepository.saveAll(Flux.just(
//                                    DataGenerator.getCommentary("1", book1.getId()),
//                                    DataGenerator.getCommentary("2", book1),
//                                    DataGenerator.getCommentary("3", book2),
//                                    DataGenerator.getCommentary("4", book3),
//                                    DataGenerator.getCommentary("5", book3),
//                                    DataGenerator.getCommentary("6", book3)
//                            ))
//                            .then();
//                }).publishOn(workerPool).subscribe();

    }

    static class DataGenerator {
        public static Author getAuthor(String id) {
            return new Author(id, "Author_" + id);
        }

        public static Genre getGenre(String id) {
            return new Genre(id, "Genre_" + id);
        }

        public static BookProjection getBook(String id, String authorId, List<String> genreId) {
            return new BookProjection(id,
                    "BookTitle_" + id,
                    authorId,
                    genreId);
        }

        public static Commentary getCommentary(String id, Book book) {
            long bookId = Long.parseLong(book.getId());
            return new Commentary(id, "comment" + bookId + id, book);
        }

    }


}
