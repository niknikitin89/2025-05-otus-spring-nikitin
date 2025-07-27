package ru.otus.hw.services.listener;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.CommentaryRepository;

@Component
@RequiredArgsConstructor
public class BookDeleteListener extends AbstractMongoEventListener<Book> {

    private final CommentaryRepository commentaryRepository;

    @Override
    public void onBeforeDelete(@Nonnull BeforeDeleteEvent<Book> event) {
        super.onBeforeDelete(event);
        String bookId = event.getSource().get("_id").toString();
        commentaryRepository.deleteAllByBookId(bookId);
    }
}
