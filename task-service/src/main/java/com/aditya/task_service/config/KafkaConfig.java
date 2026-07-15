package com.aditya.task_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Bean
   public NewTopic taskCreatedTopic(){
        return new NewTopic("task-created",1,(short) 1);
    }
}
