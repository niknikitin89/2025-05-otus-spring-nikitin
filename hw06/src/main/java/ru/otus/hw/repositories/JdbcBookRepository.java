package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final EntityManager em;

    @Override
    public Optional<Book> findById(long id) {

        TypedQuery<Book> query = em.createQuery("""
                        select distinct b from Book b
                        left join fetch b.author
                        left join fetch b.genres
                        where b.id = :id""",
                Book.class);
        query.setParameter("id", id);

        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public Optional<Book> findByIdLazy(long id) {
        return Optional.ofNullable(em.find(Book.class, id));
    }

    @Override
    public List<Book> findAll() {

        TypedQuery<Book> query = em.createQuery("""
                        select distinct b from Book b
                        left join fetch b.author
                        left join fetch b.genres""",
                Book.class);

        return query.getResultList();

    }

    @Override
    @Transactional
    public Book save(Book book) {
        if (book.getId() == 0) {
            em.persist(book);
            return book;
        } else {
            return em.merge(book);
        }
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        em.remove(em.find(Book.class, id));
    }
}
