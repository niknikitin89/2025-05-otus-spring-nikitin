package ru.otus.hw.config;

import jakarta.persistence.EntityManagerFactory;
import org.bson.types.ObjectId;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookMongo;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.GenreMongo;
import ru.otus.hw.services.IdMappingService;

@Configuration
public class BookMigrationConfig {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private IdMappingService idMappingService;

    //Reader
    @Bean
    @StepScope//бин создается не при старте приложения, а только когда начинается выполнение конкретного шага
    public JpaCursorItemReader<Book> bookReader() {

        JpaCursorItemReader<Book> reader = new JpaCursorItemReader<>();
        reader.setName("bookCursorReader");
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("select b from Book b");
        return reader;
    }

    //Processor
    @Bean
    @StepScope
    public ItemProcessor<Book, BookMongo> bookProcessor(IdMappingService idMappingService) {

        return book -> getBookMongo(book, idMappingService);
    }

    private BookMongo getBookMongo(Book book, IdMappingService idMappingService) {

        String mongoId = new ObjectId().toString();
        idMappingService.addBookMapItem(book.getId(), mongoId);
        return new BookMongo(
                mongoId,
                book.getTitle(),
                idMappingService.getAuthorId(book.getAuthor().getId()),
                book.getGenres().stream()
                        .map(Genre::getId)
                        .map(idMappingService::getGenreId)
                        .toList());
    }

    //Writer
    @Bean
    @StepScope
    public MongoItemWriter<BookMongo> bookWriter() {

        MongoItemWriter<BookMongo> writer = new MongoItemWriter<>();
        writer.setTemplate(mongoTemplate);
        writer.setCollection("books");
        writer.setMode(MongoItemWriter.Mode.INSERT);
        return writer;
    }

    //Step
    @Bean
    public Step bookMigrationStep(ItemReader<Book> reader, ItemProcessor<Book, BookMongo> processor,
                                   ItemWriter<BookMongo> writer) {

        return new StepBuilder("bookMigrationStep", jobRepository)
                .<Book, BookMongo>chunk(100, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
