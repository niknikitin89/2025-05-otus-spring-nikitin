package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.repositories.GenreRepository;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    public Flux<GenreDto> getAllGenres() {
        return genreRepository.findAll()
                .sort((o1, o2) -> o1.getId().compareTo(o2.getId()))
                .map(GenreDto::fromDomainObject);
    }
}
