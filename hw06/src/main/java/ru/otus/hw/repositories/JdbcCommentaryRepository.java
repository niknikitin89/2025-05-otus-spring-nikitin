package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Commentary;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcCommentaryRepository implements CommentaryRepository {

    private final EntityManager em;

    @Override
    public Optional<Commentary> findById(long id) {
        return Optional.ofNullable(em.find(Commentary.class, id));
    }

    @Override
    public List<Commentary> findAllByBookId(long id) {
        TypedQuery<Commentary> query =
                em.createQuery("select c from Commentary c where c.book.id = :book_id",
                        Commentary.class);
        query.setParameter("book_id", id);

        return query.getResultList();
    }

    @Override
    public Commentary save(Commentary commentary) {
        if (commentary.getId() == 0) {
            em.persist(commentary);
            return commentary;
        } else {
            return em.merge(commentary);
        }
    }

    @Override
    public void deleteById(long id) {
        em.remove(em.find(Commentary.class, id));
    }
}
