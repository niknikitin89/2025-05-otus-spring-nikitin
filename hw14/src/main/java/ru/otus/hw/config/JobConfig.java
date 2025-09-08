package ru.otus.hw.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
public class JobConfig {

    @Autowired
    private JobRepository jobRepository;
        //Job
    @Bean
    public Job migrationAuthors(Step authorMigrationStep,
                                Step genreMigrationStep,
                                Step bookMigrationStep) {

        return new JobBuilder("migration", jobRepository)
                .start(authorsAndGenresMigration(authorMigrationStep, genreMigrationStep))
                .next(bookMigrationStep)
                .build() //builds FlowJobBuilder instance
                .build();//builds Job instance
    }

    @Bean
    public Flow authorsAndGenresMigration(Step authorMigrationStep, Step genreMigrationStep) {
        return new FlowBuilder<Flow>("authorsAndGenresMigration")
                .split(new SimpleAsyncTaskExecutor())
                .add(authroMigrationFlow(authorMigrationStep), genreMigrationFlow(genreMigrationStep))
                .build();
    }

    @Bean
    public Flow authroMigrationFlow(Step authorMigrationStep){
        return new FlowBuilder<SimpleFlow>("flow1")
                .start(authorMigrationStep)
                .build();
    }

    @Bean
    public Flow genreMigrationFlow(Step genreMigrationStep){
        return new FlowBuilder<SimpleFlow>("flow1")
                .start(genreMigrationStep)
                .build();
    }
}
