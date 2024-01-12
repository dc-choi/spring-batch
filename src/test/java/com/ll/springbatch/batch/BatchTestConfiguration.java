package com.ll.springbatch.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchTestConfiguration {
    @Bean
    public JobLauncherTestUtils testMyJob(Job myJob) {
        JobLauncherTestUtils jobLauncherTestUtils = new JobLauncherTestUtils();
        jobLauncherTestUtils.setJob(myJob);
        return jobLauncherTestUtils;
    }
}
