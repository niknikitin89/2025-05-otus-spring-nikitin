package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Override
    public AuthorDto findById(long id) {
        return AuthorDto.fromDomainObject(authorRepository.findById(id).orElse(null));
    }

    @Override
    public List<AuthorDto> findAll() {

        var authors = authorRepository.findAll();

        return authors.stream().map(AuthorDto::fromDomainObject).toList();
    }
}
