package com.test.csvtodb.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BatchController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job CsvJob;

    @PostMapping("/trigger-job")
    public ResponseEntity<String> triggerBatchJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
//            JobExecution jobExecution = jobLauncher.run(CsvJob, jobParameters);
            JobExecution jobExecution = jobLauncher.run(CsvJob,jobParameters);
            return ResponseEntity.ok("Batch job started with job id: " + jobExecution.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to start batch job: " + e.getMessage());
        }
    }
}
