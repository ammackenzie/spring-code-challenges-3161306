package com.cecilireid.springchallenges;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduledTasks {

    @Autowired
    private CateringJobRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    @Scheduled(cron = "*/10 * * * * *")
    public void reportOrderStats() {
        List<CateringJob> cateringJobs = repository.findAll();
        logger.info("There are {} jobs available", cateringJobs.size());
    }
}
