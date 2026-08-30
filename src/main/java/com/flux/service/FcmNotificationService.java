package com.flux.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import org.springframework.stereotype.Service;
import com.google.firebase.messaging.Notification;

@Service
public class FcmNotificationService {

    public String sendNotification(
            String fcmToken,
            String title,
            String message
    ){
        Notification notification = Notification.builder()
                                    .setTitle(title)
                                    .setBody(message)
                                    .build();
        Message firebaseMessage = Message.builder()
                                    .setToken(fcmToken)
                                    .setNotification(notification)
                                    .build();

        try{
            return FirebaseMessaging.getInstance().send(firebaseMessage);
        }catch (Exception e){
            throw new RuntimeException("Failed to send FCM notification",e);
        }
    }
}
