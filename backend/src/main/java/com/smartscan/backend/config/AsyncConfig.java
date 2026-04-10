package com.smartscan.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(5);     // 5 parallel tasks
        executor.setMaxPoolSize(10);     // max threads
        executor.setQueueCapacity(50);   // waiting queue
        executor.setThreadNamePrefix("Async-");

        executor.initialize();
        return executor;
    }
}