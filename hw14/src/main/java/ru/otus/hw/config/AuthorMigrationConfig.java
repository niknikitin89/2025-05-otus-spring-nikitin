package ru.otus.hw.config;

import jakarta.persistence.EntityManagerFactory;
import org.bson.types.ObjectId;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
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
import ru.otus.hw.models.Author;
import ru.otus.hw.models.AuthorMongo;
import ru.otus.hw.services.IdMappingService;

@Configuration
public class AuthorMigrationConfig {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    ////Авторы
    //Reader
    @Bean
    @StepScope//бин создается не при старте приложения, а только когда начинается выполнение конкретного шага
    public JpaCursorItemReader<Author> authorReader() {

        JpaCursorItemReader<Author> reader = new JpaCursorItemReader<>();
        reader.setName("authorsCursorReader");
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("select a from Author a");
        return reader;
    }

    //Processor
    @Bean
    @StepScope
    public ItemProcessor<Author, AuthorMongo> authorProcessor(IdMappingService idMappingService) {

        return author -> getAuthorMongo(author, idMappingService);
    }

    @Bean
    public IdMappingService idMappingService() {
        return new IdMappingService();
    }

    private static AuthorMongo getAuthorMongo(Author author, IdMappingService idMappingService) {

       String mongoId = new ObjectId().toString();
        idMappingService.addAuthorMapItem(author.getId(), mongoId);

        return new AuthorMongo(mongoId, author.getFullName());
    }

    //Writer
    @Bean
    @StepScope
    public MongoItemWriter<AuthorMongo> authorWriter() {

        MongoItemWriter<AuthorMongo> writer = new MongoItemWriter<>();
        writer.setTemplate(mongoTemplate);
        writer.setCollection("authors");
        writer.setMode(MongoItemWriter.Mode.INSERT);
        return writer;
    }

    //    public ListItemWriter<AuthorMongo> authorWriter() {
//
//        return new ListItemWriter<>();
//    }

//Step
    @Bean
    public Step authorMigrationStep(ItemReader<Author> reader, ItemProcessor<Author, AuthorMongo> processor,
                                    ItemWriter<AuthorMongo> writer) {

        return new StepBuilder("authorMigrationStep", jobRepository)
                .<Author, AuthorMongo>chunk(100, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .listener(new ChunkListener(){
                    public void afterChunk(ChunkContext chunkContext) {
                        chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
                    }
                })
                .build();
    }

}
