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
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.GenreMongo;

@Configuration
public class GenreMigrationConfig {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    ////Жанры
    //Reader
    @Bean
    @StepScope//бин создается не при старте приложения, а только когда начинается выполнение конкретного шага
    public JpaCursorItemReader<Genre> genreReader() {

        JpaCursorItemReader<Genre> reader = new JpaCursorItemReader<>();
        reader.setName("genresCursorReader");
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("select g from Genre g");
        return reader;
    }

    //Processor
    @Bean
    @StepScope
    public ItemProcessor<Genre, GenreMongo> genreProcessor() {

        return genre -> {
            return new GenreMongo(new ObjectId().toString(), genre.getName());
        };
    }

    //Writer
    @Bean
    @StepScope
    public MongoItemWriter<GenreMongo> genreWriter() {

        MongoItemWriter<GenreMongo> writer = new MongoItemWriter<>();
        writer.setTemplate(mongoTemplate);
        writer.setCollection("genres");
        writer.setMode(MongoItemWriter.Mode.INSERT);
        return writer;
    }

    //Step
    @Bean
    public Step genreMigrationStep(ItemReader<Genre> reader, ItemProcessor<Genre, GenreMongo> processor,
                                   ItemWriter<GenreMongo> writer) {

        return new StepBuilder("genreMigrationStep", jobRepository)
                .<Genre, GenreMongo>chunk(100, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
