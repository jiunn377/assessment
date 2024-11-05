package com.assessment.maybank.controller;

import com.assessment.maybank.service.BatchJobService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/batch/v1")
@Tag(name = "Batch Job Controller")
public class BatchJobController {

    private final BatchJobService batchJobService;

    @Autowired
    public BatchJobController(BatchJobService batchJobService) {

        this.batchJobService = batchJobService;
    }

    @PostMapping("/runJob")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void runJob() {
        try {
            batchJobService.runJob();

        } catch (Exception e) {

            e.printStackTrace();
            throw new RuntimeException("Failed to run batch job", e);
        }
    }
}
