package ru.otus.hw.services;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class IdMappingService {

    private final Map<Long, String> authorIdMap = new ConcurrentHashMap<>();
    private final Map<Long, String> genreIdMap = new ConcurrentHashMap<>();
    private final Map<Long, String> bookIdMap = new ConcurrentHashMap<>();

    public void addAuthorMapItem(Long id, String mongoId) {

        authorIdMap.put(id, mongoId);
    }

    public void addGenreMapItem(Long id, String mongoId) {

        genreIdMap.put(id, mongoId);
    }

    public void addBookMapItem(Long id, String mongoId) {

        bookIdMap.put(id, mongoId);
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
}
