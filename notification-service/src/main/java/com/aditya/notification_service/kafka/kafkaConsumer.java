package com.aditya.notification_service.kafka;

import com.aditya.notification_service.dto.NotificationDto;
import com.aditya.notification_service.service.NotificationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class kafkaConsumer {

    private final NotificationsService notificationsService;

    @KafkaListener(topics = "task-created")
    public void consume(NotificationDto notificationDto){
        System.out.println("Inside consumer");
        notificationsService.send(notificationDto);
    }
}
