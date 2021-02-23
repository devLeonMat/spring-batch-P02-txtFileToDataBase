package com.rleon.springbatchP01txtFileToDataBase.config;

import com.rleon.springbatchP01txtFileToDataBase.listener.JobCompletionNotificationListener;
import com.rleon.springbatchP01txtFileToDataBase.model.dto.Dishes;
import com.rleon.springbatchP01txtFileToDataBase.processor.DishesItemProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Configuration
@Slf4j
@EnableBatchProcessing
public class BatchConfiguration {

    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;

    public BatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Value("${file.input}")
    private String fileInput;

    public FlatFileItemReader<Dishes> reader() {

        return new FlatFileItemReaderBuilder<Dishes>().name("dishesItemReader")
                .resource(new ClassPathResource(fileInput))
                .delimited()
                .names(new String[]{"name", "origin", "characteristics"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(Dishes.class);
                }})
                .build();
    }

    @Bean
    public DishesItemProcessor processor() {
        return new DishesItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Dishes> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Dishes>().itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO dishes (name, origin, characteristics) VALUES (:name, :origin, :characteristics)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {

        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<Dishes> writer) {
        return stepBuilderFactory.get("step1")
                .<Dishes, Dishes>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }
}
