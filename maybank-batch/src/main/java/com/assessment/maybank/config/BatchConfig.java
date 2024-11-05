package com.assessment.maybank.config;

import com.assessment.maybank.batch.TransactionItemReader;
import com.assessment.maybank.batch.TransactionItemWriter;
import com.assessment.maybank.beanio.TransactionBeanIO;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;


@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TransactionItemReader transactionItemReader;

    @Autowired
    private TransactionItemWriter transactionItemWriter;

    @Autowired
    public BatchConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job importTransactionJob() {
        return jobBuilderFactory.get("importTransactionJob")
                .incrementer(new RunIdIncrementer())
                .flow(transactionStep())
                .end()
                .build();
    }

    @Bean
    public Step transactionStep(){
        return stepBuilderFactory.get("transactionStep")
                .<TransactionBeanIO, TransactionBeanIO>chunk(10)
                .reader(transactionItemReader)
                .processor((ItemProcessor<TransactionBeanIO, TransactionBeanIO>) transaction -> transaction)
                .writer(transactionItemWriter)
                .build();
    }
}
