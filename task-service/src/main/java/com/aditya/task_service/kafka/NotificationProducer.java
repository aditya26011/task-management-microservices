package com.aditya.task_service.kafka;

import com.aditya.task_service.dtos.NotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationProducer {

    private final KafkaTemplate<Long, NotificationDto> kafkaTemplate;

    public void publish(NotificationDto notificationDto){
        System.out.println("inside publisher");
        kafkaTemplate.send("task-created",notificationDto.getUserId(),notificationDto);
    }
}
