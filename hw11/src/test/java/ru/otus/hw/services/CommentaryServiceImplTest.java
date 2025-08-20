package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.CommentaryDto;
import ru.otus.hw.models.Commentary;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentaryRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Сервис комментариев")
@DataJpaTest
//@Import({CommentaryServiceImpl.class})
@Transactional(propagation = Propagation.NEVER)
class CommentaryServiceImplTest {

//    private static final long COMMENTARY_ID = 1L;
//
//    private static final long ILLEGAL_COMMENTARY_ID = 9999L;
//
//    private static final long BOOK_ID = 3L;
//
//    private static final long ILLEGAL_BOOK_ID = 9999L;
//
//    private static final int COMMENTARY_LIST_SIZE = 3;
//
//    private static final String COMMENTARY_TEXT = "New commentary";
//
//    @Autowired
//    private CommentaryService commentaryService;
//
//    @Autowired
//    private CommentaryRepository commentaryRepository;
//
//    @Autowired
//    private BookRepository bookRepository;
//
//    @Autowired
//    private GenreRepository genreRepository;
//
//    @Test
//    void testFindByBookIdShouldReturnCommentariesList() {
//
//        List<CommentaryDto> expectedCommentList =
//                commentaryRepository.findAllByBookId(BOOK_ID).stream()
//                        .map(CommentaryDto::fromDomainObject).toList();
//
//        List<CommentaryDto> commentList = commentaryService.findByBookId(BOOK_ID);
//
//        assertThat(commentList).isNotEmpty()
//                .hasSize(COMMENTARY_LIST_SIZE)
//                .usingRecursiveAssertion().isEqualTo(expectedCommentList);
//    }
//
////    @Test
////    void testFindByBookIdShouldThrowLazyInitializationExceptionOnBookAccess() {
////
////        List<CommentaryDto> commentList = commentaryService.findByBookId(BOOK_ID);
////
////        assertThat(commentList).isNotEmpty()
////                .hasSize(COMMENTARY_LIST_SIZE);
////
////        assertThatThrownBy(() -> commentList.get(0).getBook().getTitle())
////                .isInstanceOf(LazyInitializationException.class);
////    }
//
//    @Test
//    @Transactional
//    void testAddShouldAddCommentToBook() {
//
//        var newComment = commentaryService.add(BOOK_ID, COMMENTARY_TEXT);
//
//        assertThat(newComment).isNotNull()
//                .matches(comment -> comment.getText().equals(COMMENTARY_TEXT) && comment.getId() > 0);
//    }
//
//    @Test
//    void testAddWithZeroBookIdShouldTrowsIllegalArgumentException() {
//        assertThatThrownBy(() -> commentaryService.add(0, COMMENTARY_TEXT))
//                .isInstanceOf(IllegalArgumentException.class)
//                .message().isEqualTo("Book id cannot be 0");
//    }
//
//    @Test
//    void testAddWithIllegalBookIdShouldTrowsIllegalArgumentException() {
//        assertThatThrownBy(() -> commentaryService.add(ILLEGAL_BOOK_ID, COMMENTARY_TEXT))
//                .isInstanceOf(IllegalArgumentException.class)
//                .message().isEqualTo("Book %d not found".formatted(ILLEGAL_BOOK_ID));
//    }
//
//    @Test
//    @Transactional
//    void testDeleteByIdShouldDeleteCommentary() {
//        assertThat(commentaryService.findById(COMMENTARY_ID)).isPresent();
//
//        commentaryService.deleteById(COMMENTARY_ID);
//
//        assertThat(commentaryService.findById(COMMENTARY_ID)).isEmpty();
//    }
//
//    @Test
//    @Transactional
//    void testUpdateShouldUpdateCommentary() {
//        var result = commentaryService.findById(COMMENTARY_ID);
//        assertThat(result).isPresent();
//        CommentaryDto commentary = result.get();
//
//        assertThat(commentary.getText()).isNotEqualTo(COMMENTARY_TEXT);
//
//        commentaryService.update(COMMENTARY_ID, COMMENTARY_TEXT);
//
//        result = commentaryService.findById(COMMENTARY_ID);
//        assertThat(result).isPresent();
//        commentary = result.get();
//
//        assertThat(commentary.getText()).isEqualTo(COMMENTARY_TEXT);
//    }
//
//    @Test
//    void testUpdateWithZeroIdShouldTrowsIllegalArgumentException() {
//        assertThatThrownBy(() -> commentaryService.update(0, COMMENTARY_TEXT))
//                .isInstanceOf(IllegalArgumentException.class)
//                .message().isEqualTo("Commentary id cannot be 0");
//    }
//
//    @Test
//    void testUpdateWithIllegalIdShouldTrowsIllegalArgumentException() {
//        assertThatThrownBy(() -> commentaryService.update(ILLEGAL_COMMENTARY_ID, COMMENTARY_TEXT))
//                .isInstanceOf(IllegalArgumentException.class)
//                .message().isEqualTo("Commentary %d not found".formatted(ILLEGAL_COMMENTARY_ID));
//    }
}
