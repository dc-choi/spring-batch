package com.ll.springbatch.batch;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBatchTest
@SpringBootTest(classes = { TestBatchConfiguration.class })
class JobConfigurationTest {
    @Autowired
    private Job myJob;
    @Autowired
    private JobLauncher jobLauncher;

    @Test
    public void test() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("id", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();

        JobExecution jobExecution = null;

        try {
            jobLauncher.run(myJob, jobParameters);
            assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}