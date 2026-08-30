package com.flux.service;

import com.flux.entity.Notification;
import com.flux.entity.Report;
import com.flux.entity.User;
import com.flux.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final RecipientService recipientService;
    private final FcmNotificationService fcmNotificationService;

    public NotificationService(NotificationRepository notificationRepository, RecipientService recipientService, FcmNotificationService fcmNotificationService) {
        this.notificationRepository = notificationRepository;
        this.recipientService = recipientService;
        this.fcmNotificationService = fcmNotificationService;
    }

    public List<Notification> createNotification(
            Report report,
            List<User> recipients,
            String title,
            String message) {

        List<Notification> notifications = recipients.stream()
                .filter(user ->
                        !notificationRepository.existsByReportAndUser(report, user))
                .map(user -> {

                    Notification notification = new Notification();

                    notification.setReport(report);
                    notification.setUser(user);
                    notification.setTitle(title);
                    notification.setMessage(message);
                    notification.setStatus("PENDING");
                    notification.setCreatedAt(LocalDateTime.now());

                    if (user.getFcmToken() != null &&
                            !user.getFcmToken().isBlank()) {

                        try {
                            fcmNotificationService.sendNotification(
                                    user.getFcmToken(),
                                    title,
                                    message
                            );

                            notification.setStatus("SENT");

                        } catch (Exception e) {

                            notification.setStatus("FAILED");

                            System.out.println(
                                    "FCM failed for user "
                                            + user.getId()
                                            + ": "
                                            + e.getMessage()
                            );
                        }
                    }

                    return notification;
                })
                .toList();

        if (notifications.isEmpty()) {
            return List.of();
        }

        return notificationRepository.saveAll(notifications);
    }
}
