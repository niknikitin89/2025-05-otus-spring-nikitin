package ru.otus.hw.config.batch;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.models.Commentary;
import ru.otus.hw.models.CommentaryMongo;

@Configuration
@RequiredArgsConstructor
public class CommentaryMigrationConfig {

    private final MongoTemplate mongoTemplate;

    private final JobRepository jobRepository;

    private final EntityManagerFactory entityManagerFactory;

    private final PlatformTransactionManager platformTransactionManager;

    private final IdMappingCache idMappingCache;

    //Reader
    @Bean
    @StepScope//бин создается не при старте приложения, а только когда начинается выполнение конкретного шага
    public JpaCursorItemReader<Commentary> commentaryReader() {

        JpaCursorItemReader<Commentary> reader = new JpaCursorItemReader<>();
        reader.setName("commentaryCursorReader");
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("select c from Commentary c");
        return reader;
    }

    //Processor
    @Bean
    @StepScope
    public ItemProcessor<Commentary, CommentaryMongo> commentaryProcessor() {

        return this::getcommentaryMongo;
    }

    private CommentaryMongo getcommentaryMongo(Commentary commentary) {

        String mongoId = new ObjectId().toString();
        String mongoBookId = idMappingCache.getBookId(commentary.getBookId());

        return new CommentaryMongo(
                mongoId,
                commentary.getText(),
                mongoBookId);
    }

    //Writer
    @Bean
    @StepScope
    public MongoItemWriter<CommentaryMongo> commentaryWriter() {

        MongoItemWriter<CommentaryMongo> writer = new MongoItemWriter<>();
        writer.setTemplate(mongoTemplate);
        writer.setCollection("commentaries");
        writer.setMode(MongoItemWriter.Mode.INSERT);
        return writer;
    }

    //Step
    @Bean
    public Step commentaryMigrationStep(ItemReader<Commentary> reader,
                                        ItemProcessor<Commentary, CommentaryMongo> processor,
                                        ItemWriter<CommentaryMongo> writer) {

        return new StepBuilder("commentaryMigrationStep", jobRepository)
                .<Commentary, CommentaryMongo>chunk(10, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
