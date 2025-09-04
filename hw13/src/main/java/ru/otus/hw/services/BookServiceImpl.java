package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final AclServiceWrapperService aclService;

    @Override
    public Optional<BookDto> findById(long id) {
        var bookOpt = bookRepository.findById(id);
        return bookOpt.map(BookDto::fromDomainObject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(BookDto::fromDomainObject)
                .toList();
    }

    @Override
    @Transactional
    public BookDto insert(String title, long authorId, Set<Long> genresIds) {
        return save(0, title, authorId, genresIds);
    }

    @Override
    @Transactional
    public BookDto update(long id, String title, long authorId, Set<Long> genresIds) {
        return save(id, title, authorId, genresIds);
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.models.Book', 'DELETE')")
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void save(BookDto book) {

        if (book.getId()!=0 &&
                !aclService.hasPermission(book.toDomainObject(), BasePermission.WRITE)){
            throw new AccessDeniedException("No permission to edit book");
        }

        BookDto newBookDto = this.save(
                book.getId(),
                book.getTitle(),
                book.getAuthor().getId(),
                book.getGenres().stream().map(Genre::getId).collect(Collectors.toSet()));

        if (book.getId() == 0) {
            setPermissionsForNewBook(newBookDto);
        }
    }

    private void setPermissionsForNewBook(BookDto newBookDto) {
        var newBook = newBookDto.toDomainObject();
        aclService.createPermissionForUser(newBook, BasePermission.WRITE);
        aclService.createPermissionForUser(newBook, BasePermission.DELETE);
        aclService.createPermissionForAuthority("ROLE_USER", newBook, BasePermission.READ);
        aclService.createPermissionForAuthority("ROLE_ADMIN", newBook, BasePermission.WRITE);
        aclService.createPermissionForAuthority("ROLE_ADMIN", newBook, BasePermission.READ);
        aclService.createPermissionForAuthority("ROLE_ADMIN", newBook, BasePermission.DELETE);
    }

    private BookDto save(long id, String title, long authorId, Set<Long> genresIds) {
        if (isEmpty(genresIds)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }

        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        var genres = genreRepository.findAllByIdIn(genresIds);
        if (isEmpty(genres) || genresIds.size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(genresIds));
        }

        var book = new Book(id, title, author, genres);
        return BookDto.fromDomainObject(bookRepository.save(book));
    }

}
