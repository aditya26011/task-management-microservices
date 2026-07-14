package com.aditya.notification_service.controller;

import com.aditya.notification_service.dto.NotificationDto;
import com.aditya.notification_service.service.NotificationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.bouncycastle.internal.asn1.iana.IANAObjectIdentifiers.mail;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationsService notificationsService;

    @PostMapping("/send")
    ResponseEntity<String> sendNotification(@RequestBody NotificationDto request){
        System.out.println("Inside controller");
            notificationsService.send(request);
        return new ResponseEntity<>("Sent Successfully", HttpStatus.OK);
    }
}
