package ru.otus.hw.config.batch;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class IdMappingCache {

    private final Map<Long, String> authorIdMap = new ConcurrentHashMap<>();

    private final Map<Long, String> genreIdMap = new ConcurrentHashMap<>();

    private final Map<Long, String> bookIdMap = new ConcurrentHashMap<>();

    private final Map<Long, List<Long>> booksGenres = new ConcurrentHashMap<>();

    public void addAuthorMapItem(Long id, String mongoId) {

        authorIdMap.put(id, mongoId);
    }

    public void addGenreMapItem(Long id, String mongoId) {

        genreIdMap.put(id, mongoId);
    }

    public void addBookMapItem(Long id, String mongoId) {

        bookIdMap.put(id, mongoId);
    }

    public void addBookGenreMapItem(Long bookId, Long genreId) {

        booksGenres.computeIfAbsent(bookId, k -> new ArrayList<>()).add(genreId);
    }

    public String getAuthorId(Long id) {

        return authorIdMap.get(id);
    }

    public String getGenreId(Long id) {

        return genreIdMap.get(id);
    }

    public String getBookId(Long id) {

        return bookIdMap.get(id);
    }

    public List<Long> getBookGenres(Long id) {

        return booksGenres.get(id);
    }

    public void clear() {

        authorIdMap.clear();
        genreIdMap.clear();
        bookIdMap.clear();
        booksGenres.clear();
    }
}
