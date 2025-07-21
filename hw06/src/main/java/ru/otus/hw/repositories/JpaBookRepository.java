package ru.otus.hw.repositories;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaBookRepository implements BookRepository {

    private final EntityManager em;

    private final GenreRepository genreRepository;

    @Override
    public Optional<Book> findById(long id) {

        EntityGraph<?> entityGraph = em.getEntityGraph("authors-and-genres-entity-graph");

        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.loadgraph", entityGraph);

        return Optional.ofNullable(em.find(Book.class, id, properties));
    }

    @Override
    public Optional<Book> findByIdSmall(long id) {
        return Optional.ofNullable(em.find(Book.class, id));
    }

    @Override
    public List<Book> findAll() {
        EntityGraph<?> entityGraph = em.getEntityGraph("authors-entity-graph");

        TypedQuery<Book> query = em.createQuery("select distinct b from Book b", Book.class);
        query.setHint("javax.persistence.loadgraph", entityGraph);

        return query.getResultList();
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            em.persist(book);
            return book;
        } else {
            return em.merge(book);
        }
    }

    @Override
    public void deleteById(long id) {
        em.remove(em.find(Book.class, id));
    }

}
