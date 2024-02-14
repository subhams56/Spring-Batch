package com.test.csvtodb.config;

import com.test.csvtodb.listener.JobCompletionNotificationListener;
import com.test.csvtodb.model.MyEntity;
import com.test.csvtodb.processor.CsvItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;

import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {


    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Bean
    public FlatFileItemReader<MyEntity> reader() {
        return new FlatFileItemReaderBuilder<MyEntity>()
                .name("MyEntityItemReader")
                .resource(new ClassPathResource("test.csv"))
                .delimited()
                .names("id", "name","city","age","county")
                .targetType(MyEntity.class)
                .build();
    }

    @Bean
    public CsvItemProcessor processor() {
        return new CsvItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<MyEntity> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<MyEntity>()
                .sql("INSERT INTO mytable (id, name, age, city, country) VALUES (:id, :name, :age, :city, :country)")
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }

    @Bean
    public Job CsvJob(JobRepository jobRepository, Step step1, JobCompletionNotificationListener listener) {
        return new JobBuilder("CsvJob", jobRepository)
                .listener(listener)
                .start(step1)
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, DataSourceTransactionManager transactionManager,
                      FlatFileItemReader<MyEntity> reader, CsvItemProcessor processor, JdbcBatchItemWriter<MyEntity> writer) {
        return new StepBuilder("step1", jobRepository)
                .<MyEntity, MyEntity> chunk(3, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }
}
