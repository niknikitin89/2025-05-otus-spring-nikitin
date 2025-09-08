package ru.otus.hw.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentaryRepository;
import ru.otus.hw.security.AclConfig;
import ru.otus.hw.security.CacheConfig;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest()
@Import({
        CommentaryServiceImpl.class,
        AclServiceWrapperServiceImpl.class,
        AclConfig.class,
        CacheConfig.class
})
@EnableMethodSecurity
class CommentaryServiceImplSecurityTest {

    private static final long BOOK_ID = 1L;

    private static final String COMMENT_TEXT = "Text";

    private static final String COMMENT_UPDATED_TEXT = "Updated text";

    @Autowired
    private CommentaryService commentService;

    @Autowired
    private CommentaryRepository commentaryRepository;


    public static Stream<Arguments> getUserRolesForCreation() {

        return Stream.of(
                Arguments.of("ROLE_USER"),
                Arguments.of("ROLE_ADMIN"));
    }

    public static Stream<Arguments> getUsersForCorrectWork() {

        return Stream.of(
                Arguments.of("owner", "ROLE_USER", "admin", "ROLE_ADMIN"),
                Arguments.of("admin", "ROLE_ADMIN", "admin", "ROLE_ADMIN"),
                Arguments.of("admin", "ROLE_ADMIN", "anotherAdmin", "ROLE_ADMIN"));
    }

    public static Stream<Arguments> getUsersForIncorrectWork() {

        return Stream.of(
                Arguments.of("owner", "ROLE_USER", "owner", "ROLE_USER"),
                Arguments.of("owner", "ROLE_USER", "user", "ROLE_USER"),
                Arguments.of("admin", "ROLE_ADMIN", "user", "ROLE_USER"),
                Arguments.of("owner", "ROLE_USER", "user", "ROLE_NOT_USER"),
                Arguments.of("admin", "ROLE_ADMIN", "user", "ROLE_NOT_USER"));
    }

    @ParameterizedTest
    @MethodSource("getUserRolesForCreation")
    void shouldGetAccessForAdditionComment(String ownerRole) {

        //Добавляем комментарий
        setSecurityContext("user", ownerRole);
        assertThatNoException().isThrownBy(
                () -> commentService.add(1L, "Text"));
    }

    @Test
    void shouldGetAccessDeniedForAdditionComment() {

        //Создаем книгу
        setSecurityContext("user", "ROLE_NOT_USER");
        assertThatThrownBy(() -> commentService.add(BOOK_ID, COMMENT_TEXT))
                .isInstanceOf(AccessDeniedException.class);
    }

    @ParameterizedTest
    @MethodSource("getUsersForCorrectWork")
    void shouldGetAccessForUpdateComment(
            String owner, String ownerRole, String anotherUser, String anotherUserRole) {

        //Создаем коммент от имени owner
        setSecurityContext(owner, ownerRole);
        var savedComment = commentService.add(BOOK_ID, COMMENT_TEXT);

        // Пытаемся обновить коммент от имени anotherUser
        setSecurityContext(anotherUser, anotherUserRole);

        assertThatNoException().isThrownBy(()->
                commentService.update(savedComment.getId(), COMMENT_UPDATED_TEXT));
    }


    @ParameterizedTest
    @MethodSource("getUsersForIncorrectWork")
    void shouldGetAccessDeniedForUpdateComment(
            String owner, String ownerRole, String anotherUser, String anotherUserRole) {

        //Создаем коммент от имени owner
        setSecurityContext(owner, ownerRole);
        var savedComment = commentService.add(BOOK_ID, COMMENT_TEXT);

        // Пытаемся обновить коммент от имени anotherUser
        setSecurityContext(anotherUser, anotherUserRole);

        assertThatThrownBy(() ->
                commentService.update(savedComment.getId(), COMMENT_UPDATED_TEXT))
                .isInstanceOf(AccessDeniedException.class);

    }

    @ParameterizedTest
    @MethodSource("getUsersForCorrectWork")
    void shouldGetAccessForDeleteComment(
            String owner, String ownerRole, String anotherUser, String anotherUserRole) {

        //Создаем коммент от имени owner
        setSecurityContext(owner, ownerRole);
        var savedComment = commentService.add(BOOK_ID, COMMENT_TEXT);

        // Пытаемся удалить коммент от имени anotherUser
        setSecurityContext(anotherUser, anotherUserRole);

        assertThatNoException().isThrownBy(() -> {
            commentService.deleteById(savedComment.getId());
        });
    }

    @ParameterizedTest
    @MethodSource("getUsersForIncorrectWork")
    void shouldGetAccessDeniedForDeleteComment(
            String owner, String ownerRole, String anotherUser, String anotherUserRole) {

        //Создаем коммент от имени owner
        setSecurityContext(owner, ownerRole);
        var savedComment = commentService.add(BOOK_ID, COMMENT_TEXT);

        // Пытаемся удалить коммент от имени anotherUser
        setSecurityContext(anotherUser, anotherUserRole);

        assertThatThrownBy(() -> commentService.deleteById(savedComment.getId()))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void shouldGetAccessDeniedForFindCommentById() {

        //Создаем коммент от имени owner
        setSecurityContext("user", "ROLE_USER");
        var savedComment = commentService.add(BOOK_ID, COMMENT_TEXT);
        assertThat(commentaryRepository.findById(savedComment.getId()))
                .isNotEmpty();

        setSecurityContext("someone", "ROLE_NOT_USER");
        assertThatThrownBy(() -> commentService.findById(savedComment.getId()))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void shouldNotGetCommentsForFindByBookId() {

        //Создаем коммент от имени owner
        setSecurityContext("user", "ROLE_USER");
        var savedComment = commentService.add(BOOK_ID, COMMENT_TEXT);
        assertThat(commentaryRepository.findById(savedComment.getId()))
                .isNotEmpty();

        setSecurityContext("someone", "ROLE_NOT_USER");
        assertThat(commentService.findByBookId(BOOK_ID)).isEmpty();
    }

    private void setSecurityContext(String username, String... roles) {

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                username,
                "password",
                Arrays.stream(roles)
                        .map(SimpleGrantedAuthority::new)
                        .toList()
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
}
