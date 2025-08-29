package ru.otus.hw;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Scheduler;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;
import ru.otus.hw.projections.BookProjection;
import ru.otus.hw.projections.CommentaryProjection;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentaryRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.Arrays;
import java.util.List;

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

        dropDB();

        fillAuthors();

        fillGenres();

        fillBooks();

        fillComments();

    }

    private void fillComments() {
        commentaryRepository.saveAll(Arrays.asList(
                DataGenerator.getCommentary("1", "1"),
                DataGenerator.getCommentary("2", "1"),
                DataGenerator.getCommentary("3", "2"),
                DataGenerator.getCommentary("4", "3"),
                DataGenerator.getCommentary("5", "3"),
                DataGenerator.getCommentary("6", "3")
        )).publishOn(workerPool).subscribe();
    }

    private void fillBooks() {
        bookRepository.saveAll(Arrays.asList(
                DataGenerator.getBook("1", "1", List.of("1", "2")),
                DataGenerator.getBook("2", "2", List.of("3", "4")),
                DataGenerator.getBook("3", "3", List.of("5", "6"))
        )).publishOn(workerPool).subscribe();
    }

    private void fillGenres() {
        genreRepository.saveAll(Arrays.asList(
                DataGenerator.getGenre("2"),
                DataGenerator.getGenre("1"),
                DataGenerator.getGenre("3"),
                DataGenerator.getGenre("4"),
                DataGenerator.getGenre("5"),
                DataGenerator.getGenre("6")
        )).publishOn(workerPool).subscribe();
    }

    private void fillAuthors() {
        authorRepository.saveAll(Arrays.asList(
                DataGenerator.getAuthor("1"),
                DataGenerator.getAuthor("2"),
                DataGenerator.getAuthor("3")
        )).publishOn(workerPool).subscribe();
    }

    private void dropDB() {
        authorRepository.deleteAll().publishOn(workerPool).subscribe();
        genreRepository.deleteAll().publishOn(workerPool).subscribe();
        bookRepository.deleteAll().publishOn(workerPool).subscribe();
        commentaryRepository.deleteAll().publishOn(workerPool).subscribe();
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

        public static CommentaryProjection getCommentary(String id, String bookId) {
            return new CommentaryProjection(id, "comment" + bookId + id, bookId);
        }

    }


}
