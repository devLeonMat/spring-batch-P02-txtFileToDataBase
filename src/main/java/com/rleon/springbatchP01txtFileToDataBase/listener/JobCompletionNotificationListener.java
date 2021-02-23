package com.rleon.springbatchP01txtFileToDataBase.listener;

import com.rleon.springbatchP01txtFileToDataBase.model.dto.Dishes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");
            String query = "SELECT name, origin, characteristics FROM dishes";
            jdbcTemplate.query(query, (rs, row) -> new Dishes(rs.getString(1), rs.getString(2), rs.getString(3)))
                    .forEach(dishes -> log.info("Found < {} > in the database.", dishes));
        }

        super.afterJob(jobExecution);
    }
}
