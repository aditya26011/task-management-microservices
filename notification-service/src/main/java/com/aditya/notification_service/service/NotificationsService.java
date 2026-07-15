package com.aditya.notification_service.service;

import com.aditya.notification_service.dto.NotificationDto;
import com.aditya.notification_service.entity.Notification;
import com.aditya.notification_service.repo.NotificationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationsService {


    private final JavaMailSender javaMailSender;
    private final NotificationRepo notificationRepo;

    @KafkaListener(topics = "task-created")
    public void send(NotificationDto request) {

        SimpleMailMessage mail=new SimpleMailMessage();
        mail.setTo(request.getEmail());
        mail.setSubject(request.getSubject());
        mail.setText(request.getMessage());

        Notification notification=new Notification();
        notification.setSubject(request.getSubject());
        notification.setMessage(request.getMessage());
        notification.setUserId(request.getUserId());
        notification.setCreatedAt(LocalDateTime.now());
        notificationRepo.save(notification);

        javaMailSender.send(mail);
    }
}
