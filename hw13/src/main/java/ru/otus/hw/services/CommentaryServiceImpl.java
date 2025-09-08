package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.CommentaryDto;
import ru.otus.hw.models.Commentary;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentaryRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentaryServiceImpl implements CommentaryService {

    private final CommentaryRepository commentaryRepository;

    private final BookRepository bookRepository;

    private final AclServiceWrapperService aclService;

    @Override
    @Transactional(readOnly = true)
    public List<CommentaryDto> findByBookId(long id) {

        return commentaryRepository.findAllByBookId(id).stream()
                .filter(comm -> aclService.hasPermission(comm, BasePermission.READ))
                .map(CommentaryDto::fromDomainObject).toList();
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.models.Commentary', 'READ')")
    public Optional<CommentaryDto> findById(long id) {

        var comment = commentaryRepository.findById(id);
        return comment.map(CommentaryDto::fromDomainObject);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.models.Commentary', 'READ')")
    public Optional<CommentaryDto> findByIdWithBook(long id) {

        var comment = commentaryRepository.findById(id);
        return comment.map(CommentaryDto::fromDomainObjectWithBook);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public CommentaryDto add(long bookId, String text) {

        if (bookId == 0) {
            throw new IllegalArgumentException("Book id cannot be 0");
        }

        var book = bookRepository.findByIdSmall(bookId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Book %d not found".formatted(bookId)));

        Commentary commentary = new Commentary(0, text, book);
        commentary = commentaryRepository.save(commentary);

        setPermissionsForNewComment(commentary);

        return CommentaryDto.fromDomainObject(commentary);
    }

    private void setPermissionsForNewComment(Commentary commentary) {
        //Юзеры только читают
        aclService.createPermissionForAuthority("ROLE_USER", commentary, BasePermission.READ);
        aclService.createPermissionForAuthority("ROLE_ADMIN", commentary, BasePermission.WRITE);
        aclService.createPermissionForAuthority("ROLE_ADMIN", commentary, BasePermission.READ);
        aclService.createPermissionForAuthority("ROLE_ADMIN", commentary, BasePermission.DELETE);
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.models.Commentary', 'DELETE')")
    public void deleteById(long id) {

        commentaryRepository.deleteById(id);
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.models.Commentary', 'WRITE')")
    public void update(long id, String text) {

        if (id == 0) {
            throw new IllegalArgumentException("Commentary id cannot be 0");
        }

        var commentary = commentaryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Commentary %d not found".formatted(id)));

        commentary.setText(text);
        commentaryRepository.save(commentary);
    }


}
