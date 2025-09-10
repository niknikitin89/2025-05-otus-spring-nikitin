package ru.otus.hw.config.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.models.BookGenre;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Configuration
@RequiredArgsConstructor
public class BooksGenresMigrationConfig {

    private final JobRepository jobRepository;

    private final DataSource dataSource;

    private final PlatformTransactionManager platformTransactionManager;

    private final IdMappingCache idMappingCache;

    //Reader
    @Bean
    @StepScope//бин создается не при старте приложения, а только когда начинается выполнение конкретного шага
    public JdbcCursorItemReader<BookGenre> bookGenreReader() {

        JdbcCursorItemReader<BookGenre> reader = new JdbcCursorItemReader<>();
        reader.setName("booksGenresCursorReader");
        reader.setDataSource(dataSource);
        reader.setSql("select * from books_genres");
        reader.setRowMapper(new RowMapper<BookGenre>() {
            @Override
            public BookGenre mapRow(ResultSet rs, int rowNum) throws SQLException {

                long bookId = rs.getLong("book_id");
                long genreId = rs.getLong("genre_id");
                return new BookGenre(bookId, genreId);
            }
        });
        return reader;
    }

    //Processor
    @Bean
    @StepScope
    public ItemProcessor<BookGenre, BookGenre> bookGenreProcessor() {

        return bookGenre -> {
            idMappingCache.addBookGenreMapItem(bookGenre.bookId(), bookGenre.genreId());
            return bookGenre;
        };
    }

    //Step
    @Bean
    public Step booksGenresMigrationStep(ItemReader<BookGenre> reader,
                                         ItemProcessor<BookGenre, BookGenre> processor) {

        return new StepBuilder("booksGenresMigrationStep", jobRepository)
                .<BookGenre, BookGenre>chunk(10, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(i -> {
                })//ничего никуда не пишем
                .build();
    }
}
