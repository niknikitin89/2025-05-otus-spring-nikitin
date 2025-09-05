package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
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
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.security.AclConfig;
import ru.otus.hw.security.CacheConfig;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest()
@Import({
        BookServiceImpl.class,
        AclServiceWrapperServiceImpl.class,
        AclConfig.class,
        CacheConfig.class
})
@EnableMethodSecurity
class BookServiceImplSecurityTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private BookRepository bookRepository;

    private BookDto newBookDto;

    public static Stream<Arguments> getUserRolesForCreation() {

        return Stream.of(
                Arguments.of("ROLE_USER"),
                Arguments.of("ROLE_ADMIN"));
    }

    public static Stream<Arguments> getUsersForCorrectWork() {

        return Stream.of(
                Arguments.of("owner", "ROLE_USER", "owner", "ROLE_USER"),
                Arguments.of("owner", "ROLE_USER", "admin", "ROLE_ADMIN"),
                Arguments.of("admin", "ROLE_ADMIN", "admin", "ROLE_ADMIN"),
                Arguments.of("admin", "ROLE_ADMIN", "anotherAdmin", "ROLE_ADMIN"));
    }

    public static Stream<Arguments> getUsersForIncorrectWork() {

        return Stream.of(
                Arguments.of("owner", "ROLE_USER", "user", "ROLE_USER"),
                Arguments.of("admin", "ROLE_ADMIN", "user", "ROLE_USER"),
                Arguments.of("owner", "ROLE_USER", "user", "ROLE_NOT_USER"),
                Arguments.of("admin", "ROLE_ADMIN", "user", "ROLE_NOT_USER"));
    }

    @BeforeEach
    void setUp() {

        Author author = authorRepository.findById(1L).get();
        Genre genre = genreRepository.findById(1L).get();
        newBookDto = new BookDto(0, "Book ", author, List.of(genre));
    }

    @ParameterizedTest
    @MethodSource("getUserRolesForCreation")
    void shouldGetAccessForCreateBook(String ownerRole) {

        //Создаем книгу
        setSecurityContext("user", ownerRole);
        assertThatNoException().isThrownBy(
                () -> bookService.save(newBookDto));
    }

    @Test
    void shouldGetAccessDeniedForCreateBook() {

        //Создаем книгу
        setSecurityContext("user", "ROLE_NOT_USER");
        assertThatThrownBy(() -> bookService.save(newBookDto))
                .isInstanceOf(AccessDeniedException.class);
    }

    @ParameterizedTest
    @MethodSource("getUsersForCorrectWork")
    void shouldGetAccessForUpdateBook(
            String owner, String ownerRole, String anotherUser, String anotherUserRole) {

        //Создаем книгу от имени owner
        setSecurityContext(owner, ownerRole);
        var savedBook = bookService.save(newBookDto);

        // Пытаемся обновить книгу от имени anotherUser
        setSecurityContext(anotherUser, anotherUserRole);
        savedBook.setTitle("Updated Book");
        var updatedBook = bookService.save(savedBook);

        assertThat(updatedBook.getTitle()).isEqualTo("Updated Book");
    }

    @ParameterizedTest
    @MethodSource("getUsersForIncorrectWork")
    void shouldGetAccessDeniedForUpdateBook(
            String owner, String ownerRole, String anotherUser, String anotherUserRole) {

        //Создаем книгу от имени owner
        setSecurityContext(owner, ownerRole);
        var savedBook = bookService.save(newBookDto);

        // Пытаемся обновить книгу от имени anotherUser
        setSecurityContext(anotherUser, anotherUserRole);
        savedBook.setTitle("Updated Book");
        assertThatThrownBy(() -> bookService.save(savedBook))
                .isInstanceOf(AccessDeniedException.class);

    }

    @ParameterizedTest
    @MethodSource("getUsersForCorrectWork")
    void shouldGetAccessForDeleteBook(
            String owner, String ownerRole, String anotherUser, String anotherUserRole) {

        //Создаем книгу от имени owner
        setSecurityContext(owner, ownerRole);
        var savedBook = bookService.save(newBookDto);

        // Пытаемся удалить книгу от имени anotherUser
        setSecurityContext(anotherUser, anotherUserRole);
        assertThatNoException().isThrownBy(() -> {
            bookService.deleteById(savedBook.getId());
        });
    }

    @ParameterizedTest
    @MethodSource("getUsersForIncorrectWork")
    void shouldGetAccessDeniedForDeleteBook(
            String owner, String ownerRole, String anotherUser, String anotherUserRole) {

        //Создаем книгу от имени owner
        setSecurityContext(owner, ownerRole);
        var savedBook = bookService.save(newBookDto);

        // Пытаемся удалить книгу от имени anotherUser
        setSecurityContext(anotherUser, anotherUserRole);
        assertThatThrownBy(() -> bookService.deleteById(savedBook.getId()))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void shouldGetAccessDeniedForFindBookById() {

        //Создаем книгу от имени owner
        setSecurityContext("user", "ROLE_USER");
        var savedBook = bookService.save(newBookDto);
        assertThat(bookRepository.findByIdSmall(savedBook.getId())).isNotEmpty();

        setSecurityContext("someone", "ROLE_NOT_USER");
        assertThatThrownBy(() -> bookService.findById(savedBook.getId()))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void shouldNotGetBooksForFindAll() {

        //Создаем книгу от имени owner
        setSecurityContext("user", "ROLE_USER");
        var savedBook = bookService.save(newBookDto);
        assertThat(bookRepository.findByIdSmall(savedBook.getId())).isNotEmpty();

        setSecurityContext("someone", "ROLE_NOT_USER");
        assertThat(bookService.findAll()).isEmpty();
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
