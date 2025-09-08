package ru.otus.hw.services;

import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class IdMappingService {

    private final Map<Long, String> authorIdMap = new ConcurrentHashMap<>();

    public void addAuthorMapItem(Long id, String mongoId) {

        authorIdMap.put(id, mongoId);
    }

    public Map<Long, String> getAuthorIdMap() {
        return authorIdMap;
    }
}
