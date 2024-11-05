package com.assessment.maybank.service;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class
BatchJobService {

    private final JobLauncher jobLauncher;
    private final Job importTransactionJob;

    @Autowired
    public BatchJobService(JobLauncher jobLauncher, Job importTransactionJob) {
        this.jobLauncher = jobLauncher;
        this.importTransactionJob = importTransactionJob;
    }

    public void runJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();

        jobLauncher.run(importTransactionJob, jobParameters);
    }
}