package ru.otus.hw.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Commentary;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentaryRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Set;

@ChangeLog
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "dropDb", author = "nyunikitin", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertAuthors", author = "nyunikitin")
    public void insertAuthors(AuthorRepository authorRepository) {
        authorRepository.save(DataGenerator.getAuthor("1"));
        authorRepository.save(DataGenerator.getAuthor("2"));
        authorRepository.save(DataGenerator.getAuthor("3"));
    }

    @ChangeSet(order = "003", id = "insertGenres", author = "nyunikitin")
    public void insertGenres(GenreRepository genreRepository) {
        genreRepository.save(DataGenerator.getGenre("1"));
        genreRepository.save(DataGenerator.getGenre("2"));
        genreRepository.save(DataGenerator.getGenre("3"));
        genreRepository.save(DataGenerator.getGenre("4"));
        genreRepository.save(DataGenerator.getGenre("5"));
        genreRepository.save(DataGenerator.getGenre("6"));
    }

    @ChangeSet(order = "004", id = "insertBooks", author = "nyunikitin")
    public void insertBooks(BookRepository bookRepository) {
        bookRepository.save(DataGenerator.getBook("1", "1", Set.of("1", "2")));
        bookRepository.save(DataGenerator.getBook("2", "2", Set.of("3", "4")));
        bookRepository.save(DataGenerator.getBook("3", "3", Set.of("5", "6")));
    }

    @ChangeSet(order = "005", id = "insertCommentaries", author = "nyunikitin")
    public void insertCommentaries(CommentaryRepository commentaryRepository, BookRepository bookRepository) {
        var book1 = bookRepository.findById("1");
        var book2 = bookRepository.findById("2");
        var book3 = bookRepository.findById("3");

        if (book1.isEmpty() || book2.isEmpty() || book3.isEmpty()) {
            throw new IllegalStateException("No books in database");
        }

        commentaryRepository.save(DataGenerator.getCommentary("1", book1.get()));
        commentaryRepository.save(DataGenerator.getCommentary("2", book1.get()));
        commentaryRepository.save(DataGenerator.getCommentary("3", book2.get()));
        commentaryRepository.save(DataGenerator.getCommentary("4", book3.get()));
        commentaryRepository.save(DataGenerator.getCommentary("5", book3.get()));
        commentaryRepository.save(DataGenerator.getCommentary("6", book3.get()));
    }

    static class DataGenerator {
        public static Author getAuthor(String id) {
            return new Author(id, "Author_" + id);
        }

        public static Genre getGenre(String id) {
            return new Genre(id, "Genre_" + id);
        }

        public static Book getBook(String id, String authorId, Set<String> genreId) {
            List<Genre> genres = genreId.stream().map(DataGenerator::getGenre).toList();
            return new Book(id,
                    "BookTitle_" + id,
                    getAuthor(authorId),
                    genres);
        }

        public static Commentary getCommentary(String id, Book book) {
            long bookId = Long.parseLong(book.getId());
            return new Commentary(id, "comment" + bookId + id, book);
        }

    }


}
