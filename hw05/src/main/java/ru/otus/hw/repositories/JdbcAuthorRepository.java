package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcAuthorRepository implements AuthorRepository {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public List<Author> findAll() {

        return jdbc.query(
                "select id, full_name from authors",
                new AuthorRowMapper());
    }

    @Override
    public Optional<Author> findById(long id) {

        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        List<Author> result = jdbc.query(
                "select id, full_name from authors where id = :id",
                params, new AuthorRowMapper());

        return result.isEmpty() ?
                Optional.empty() : Optional.of(result.get(0));
    }

    private static class AuthorRowMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet rs, int i) throws SQLException {
            long id = rs.getLong("id");
            String fullName = rs.getString("full_name");
            return new Author(id, fullName);
        }
    }
}
