package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
//    private final AuthorRepository authorRepository;

    @Override
    public Optional<AuthorDto> findById(long id) {
//        var authorOpt = authorRepository.findById(id);
//        return authorOpt.map(AuthorDto::fromDomainObject);
        return null;
    }

    @Override
    public List<AuthorDto> findAll() {

//        var authors = authorRepository.findAll();
//
//        return authors.stream().map(AuthorDto::fromDomainObject).toList();
        return null;
    }
}
