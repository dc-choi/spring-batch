package com.ll.springbatch.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
public class JobConfiguration {
    @Bean
    public Job myJob(JobRepository jobRepository, Step myStep) {
        return new JobBuilder("myJob", jobRepository)
                .start(myStep)
                .incrementer(new RunIdIncrementer()) // 배치가 자동으로 돌아갈 때만 작동함.
                .build();
    }

    @Bean
    public Step myStep(JobRepository jobRepository, Tasklet myTasklet, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("myTasklet", jobRepository)
                .tasklet(myTasklet, platformTransactionManager).build();
    }

    @Bean
    public Tasklet myTasklet() {
        return ((contribution, chunkContext) -> {
            log.info("Hello World");
            return RepeatStatus.FINISHED;
        });
    }
}
