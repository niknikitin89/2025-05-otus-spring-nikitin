package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Commentary;
import ru.otus.hw.models.Genre;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
class TestDataManager {

    @Autowired
    MongoTemplate mt;

    public void dropDb() {
        mt.getDb().drop();
    }


    public Author saveAuthor(String fullName) {
        return mt.save(new Author(fullName));
    }

    public Genre saveGenre(String name) {
        return mt.save(new Genre(name));
    }

    public Book saveBook(String title, Author author, List<Genre> genres) {
        return mt.save(new Book(title, author, genres));
    }

    public Commentary saveCommentary(String text, Book book) {

        return mt.save(new Commentary(text, book));
    }

    public Book generateBook(){
        var author = saveAuthor("Author");

        List<Genre> genres = new ArrayList<>();
        var genre = saveGenre("Genre1");
        genres.add(genre);
        genre = saveGenre("Genre2");
        genres.add(genre);

        return saveBook("Title", author, genres);
    }

}
