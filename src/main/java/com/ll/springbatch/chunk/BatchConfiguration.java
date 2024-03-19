package com.ll.springbatch.chunk;

import com.ll.springbatch.test.Test;
import com.ll.springbatch.test.TestRepository;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 1. Chunk 방식
 *
 * 대량의 데이터를 작은 단위(chunk)로 나누어 처리합니다.
 * 각 chunk는 reader, processor, writer의 세 가지 주요 단계로 처리됩니다.
 * Reader: 데이터 소스에서 데이터를 읽어옵니다.
 * Processor: 읽어온 데이터를 가공하거나 변환합니다.
 * Writer: 가공된 데이터를 특정 대상에 쓰거나 전송합니다.
 * 대량의 데이터를 효율적으로 처리할 수 있습니다.
 * 트랜잭션 관리가 용이합니다.
 * 장애 발생 시 chunk 단위로 재시작할 수 있습니다.
 *
 * 2. Tasklet 방식
 * 단일 작업 단위(tasklet)로 처리됩니다.
 * tasklet은 일련의 비즈니스 로직을 포함하고 있는 단일 메서드입니다.
 * tasklet은 한 번에 모든 데이터를 처리합니다.
 * 대량의 데이터를 처리하기에는 적합하지 않을 수 있습니다.
 * 트랜잭션 관리가 상대적으로 어렵습니다.
 * 장애 발생 시 전체 tasklet을 재실행해야 합니다.
 * chunk 방식과 tasklet 방식의 비교
 *
 * chunk 방식은 대량의 데이터를 효율적으로 처리하는 데 적합합니다.
 * tasklet 방식은 상대적으로 간단한 작업이나 제어 흐름 로직에 적합합니다.
 * chunk 방식은 트랜잭션 관리와 장애 복구가 용이합니다.
 * tasklet 방식은 구현이 간단하지만, 대량의 데이터 처리에는 적합하지 않습니다.
 * 일반적으로 대량의 데이터를 처리해야 하는 경우에는 chunk 방식을, 단순한 작업이나 제어 흐름 로직이 필요한 경우에는 tasklet 방식을 사용하는 것이 좋습니다. 또한, 두 가지 방식을 혼합하여 사용할 수도 있습니다.
 */
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class BatchConfiguration {
    private final TestRepository testRepository;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job myJob(JobRepository jobRepository, Step myStep) {
        return new JobBuilder("myJob", jobRepository)
                .start(myStep)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step myStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("myChunk", jobRepository)
                .<Test, Test>chunk(10, platformTransactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public JpaPagingItemReader<Test> reader() {
        return new JpaPagingItemReaderBuilder<Test>()
                .name("reader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(10)
                .queryString("SELECT id FROM Test t")
                .build();
    }

    @Bean
    public ItemProcessor<Test, Test> processor() {
        return test -> {
            System.out.println("processor");
            return test;
        };
    }

    @Bean
    public ItemWriter<Test> writer() {
        return tests -> {
            System.out.println("writer");
            for (Test test : tests) {
                System.out.println("writer");
            }
        };
    }
}
