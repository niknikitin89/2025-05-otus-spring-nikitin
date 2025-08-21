package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Commentary;

@RequiredArgsConstructor
public class CustomCommentaryRepositoryImpl implements CustomCommentaryRepository {

//    private final R2dbcEntityTemplate entityTemplate;

    private final BookRepository bookRepository;

    @Override
    public Mono<Commentary> findByIdWithBook(Long id) {
//        return entityTemplate.select(Commentary.class)
//                .matching(Query.query(Criteria.where("id").is(id)))
//                .one()
//                .flatMap(this::loadBookDetails);
        return null;
    }

    private Mono<Commentary> loadBookDetails(Commentary commentary) {
//        return bookRepository.findById(commentary.getBookId())
//                .defaultIfEmpty(new Book()) // Чтобы избежать NPE
//                .map(book -> {
//                    commentary.setBook(book);
//                    return commentary;
//                });
        return null;
    }
}
