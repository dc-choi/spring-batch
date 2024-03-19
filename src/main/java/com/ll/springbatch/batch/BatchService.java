//package com.ll.springbatch.batch;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.JobParameters;
//import org.springframework.batch.core.JobParametersBuilder;
//import org.springframework.batch.core.launch.JobLauncher;
//import org.springframework.stereotype.Service;
//
///**
// * 배치는 자동으로 트랜잭션이 적용된다.
// */
//@Service
//@RequiredArgsConstructor
//public class BatchService {
//    private final JobLauncher jobLauncher;
//    private final Job myJob;
//
//    public void runSimpleJob() {
//        try {
//            JobParameters jobParameters = new JobParametersBuilder()
//                    // 사용자가 임의로 생성할 경우는 임의로 생성 필요
//                    .addString("id", String.valueOf(System.currentTimeMillis()))
//                    .toJobParameters();
//
//            jobLauncher.run(myJob, jobParameters);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
