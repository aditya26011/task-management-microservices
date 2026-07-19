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

    public void send(NotificationDto request) {

        SimpleMailMessage mail=new SimpleMailMessage();
        mail.setTo(request.getEmail());
        mail.setSubject(request.getSubject());
        mail.setText(request.getMessage());
        System.out.println("Inside notification service");
        notificationRepo.save(mapToNotification(request));
        javaMailSender.send(mail);
    }

    private Notification mapToNotification(NotificationDto dto) {

        Notification notification = new Notification();
        notification.setUserId(dto.getUserId());
        notification.setSubject(dto.getSubject());
        notification.setMessage(dto.getMessage());
        notification.setCreatedAt(LocalDateTime.now());

        return notification;
    }
}
