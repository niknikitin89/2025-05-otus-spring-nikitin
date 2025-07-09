package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcTemplate jdbc;

    private final GenreRepository genreRepository;

    @Override
    public Optional<Book> findById(long id) {

        Book book = jdbc.query(
                """
                        select b.id, b.title, b.author_id, a.full_name, g.id as genre_id, g.name as genre_name 
                        from books as b 
                        left join authors as a on b.author_id = a.id 
                        left join books_genres as bg on b.id = bg.book_id 
                        left join genres as g on bg.genre_id = g.id 
                        where b.id = :id""",
                new MapSqlParameterSource("id", id),
                new BookResultSetExtractor());

        return book == null ? Optional.empty() : Optional.of(book);
    }

    @Override
    public List<Book> findAll() {
        var genres = genreRepository.findAll();
        var relations = getAllGenreRelations();
        var books = getAllBooksWithoutGenres();
        mergeBooksInfo(books, genres, relations);
        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        jdbc.update("delete from books where id = :id",
                new MapSqlParameterSource("id", id));
    }

    private List<Book> getAllBooksWithoutGenres() {

        return jdbc.query(
                """
                        select b.id, b.title, b.author_id, a.full_name 
                        from books as b 
                        left join authors as a 
                        on b.author_id = a.id""",
                new BookRowMapper());
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        return jdbc.query("select book_id, genre_id from books_genres",
                (rs, rowNum) -> new BookGenreRelation(
                        rs.getLong("book_id"), rs.getLong("genre_id")));
    }

    private void mergeBooksInfo(List<Book> booksWithoutGenres, List<Genre> genres, List<BookGenreRelation> relations) {

        Map<Long, Genre> genresMap = genres.stream()
                .collect(Collectors.toMap(Genre::getId, g -> g));

        Map<Long, List<Genre>> bookAndGenresMap = new HashMap<>();
        for (BookGenreRelation relation : relations) {
            Genre genreForBook = genresMap.get(relation.genreId);

            if (bookAndGenresMap.containsKey(relation.bookId)) {
                List<Genre> genreList = bookAndGenresMap.get(relation.bookId);
                genreList.add(genreForBook);
            } else {
                bookAndGenresMap
                        .put(relation.bookId, new ArrayList<>(Arrays.asList(genreForBook)));
            }
        }

        for (Book book : booksWithoutGenres) {
            book.setGenres(bookAndGenresMap.get(book.getId()));
        }
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle());
        params.addValue("author_id", book.getAuthor().getId());

        jdbc.update(
                "insert into books (title, author_id) " +
                        "values (:title, :author_id)",
                params, keyHolder, new String[]{"id"});

        book.setId(keyHolder.getKeyAs(Long.class));
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private Book update(Book book) {
        var params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle());
        params.addValue("author_id", book.getAuthor().getId());
        params.addValue("id", book.getId());

        int rowsUpdated = jdbc.update("""
                        update books set title = :title, author_id = :author_id 
                        where id = :id""",
                params);

        if (rowsUpdated == 0) {
            throw new EntityNotFoundException("Book with id " + book.getId() + " not found");
        }

        removeGenresRelationsFor(book);
        batchInsertGenresRelationsFor(book);

        return book;
    }

    private void batchInsertGenresRelationsFor(Book book) {
        MapSqlParameterSource[] params = new MapSqlParameterSource[book.getGenres().size()];
        for (int i = 0; i < book.getGenres().size(); i++) {
            params[i] = new MapSqlParameterSource();
            params[i].addValue("book_id", book.getId());
            params[i].addValue("genre_id", book.getGenres().get(i).getId());
        }

        jdbc.batchUpdate("""
                        insert into books_genres (book_id, genre_id) 
                        values (:book_id, :genre_id)""",
                params);
    }

    private void removeGenresRelationsFor(Book book) {
        jdbc.update("delete from books_genres where book_id = :book_id",
                new MapSqlParameterSource("book_id", book.getId()));
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String title = rs.getString("title");
            Author author = new Author(rs.getLong("author_id"), rs.getString("full_name"));

            return new Book(id, title, author, null);
        }
    }

    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class BookResultSetExtractor implements ResultSetExtractor<Book> {

        @Override
        public Book extractData(ResultSet rs) throws SQLException, DataAccessException {
            Book book = null;
            while (rs.next()) {
                if (book == null) {
                    book = new Book();
                    book.setId(rs.getLong("id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(new Author(
                            rs.getLong("author_id"), rs.getString("full_name")));
                    List<Genre> genres = new ArrayList<>();
                    genres.add(new Genre(
                            rs.getLong("genre_id"), rs.getString("genre_name")));
                    book.setGenres(genres);
                } else {
                    List<Genre> genres = book.getGenres();
                    genres.add(new Genre(
                            rs.getLong("genre_id"), rs.getString("genre_name")));
                }
            }
            return book;
        }
    }

    private record BookGenreRelation(long bookId, long genreId) {
    }
}
