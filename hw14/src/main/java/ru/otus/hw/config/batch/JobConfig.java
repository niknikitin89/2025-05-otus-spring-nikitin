package ru.otus.hw.config.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
@RequiredArgsConstructor
public class JobConfig {

    private final JobRepository jobRepository;

    //Job
    @Bean
    public Job migrationJob(Step authorMigrationStep,
                            Step genreMigrationStep,
                            Step booksGenresMigrationStep,
                            Step bookMigrationStep,
                            Step commentaryMigrationStep) {

        return new JobBuilder("migration", jobRepository)
                .start(parallelSteps(authorMigrationStep, genreMigrationStep, booksGenresMigrationStep))
                .next(bookMigrationStep)
                .next(commentaryMigrationStep)
                .build() //builds FlowJobBuilder instance
                .build();//builds Job instance
    }

    @Bean
    public Flow parallelSteps(Step authorMigrationStep, Step genreMigrationStep, Step booksGenresMigrationStep) {

        return new FlowBuilder<Flow>("authorsAndGenresMigration")
                .split(new SimpleAsyncTaskExecutor())
                .add(
                        new FlowBuilder<Flow>("authorFlow")
                                .start(authorMigrationStep)
                                .build(),
                        new FlowBuilder<Flow>("genreFlow")
                                .start(genreMigrationStep)
                                .build(),
                        new FlowBuilder<Flow>("booksGenresMigration")
                                .start(booksGenresMigrationStep)
                                .build())
                .build();
    }
}
