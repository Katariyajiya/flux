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
    private final FcmNotificationService fcmNotificationService;

    public NotificationService(
            NotificationRepository notificationRepository,
            FcmNotificationService fcmNotificationService) {
        this.notificationRepository = notificationRepository;
        this.fcmNotificationService = fcmNotificationService;
    }

    public List<Notification> createNotification(
            Report report,
            List<User> recipients,
            String title,
            String message) {

        System.out.println(
                ">>> NOTIFICATION FLOW REACHED - REPORT: "
                        + report.getReportId());

        System.out.println(
                ">>> RECIPIENT COUNT: " + recipients.size());

        List<Notification> notifications = recipients.stream()
                .filter(user -> {
                    boolean exists =
                            notificationRepository.existsByReportAndUser(
                                    report, user);

                    System.out.println(
                            ">>> USER " + user.getId()
                                    + " / " + user.getEmail()
                                    + " | EXISTS: " + exists);

                    return !exists;
                })
                .map(user -> {

                    System.out.println(
                            ">>> CREATING NOTIFICATION FOR USER: "
                                    + user.getId()
                                    + " / " + user.getEmail());

                    Notification notification = new Notification();

                    notification.setReport(report);
                    notification.setUser(user);
                    notification.setTitle(title);
                    notification.setMessage(message);
                    notification.setStatus("PENDING");
                    notification.setCreatedAt(LocalDateTime.now());

                    if (user.getFcmToken() != null
                            && !user.getFcmToken().isBlank()) {

                        try {
                            fcmNotificationService.sendNotification(
                                    user.getFcmToken(),
                                    title,
                                    message
                            );

                            notification.setStatus("SENT");

                            System.out.println(
                                    ">>> FCM SENT TO USER: "
                                            + user.getId());

                        } catch (Exception e) {

                            notification.setStatus("FAILED");

                            System.out.println(
                                    ">>> FCM FAILED FOR USER: "
                                            + user.getId());

                            e.printStackTrace();
                        }

                    } else {

                        System.out.println(
                                ">>> NO FCM TOKEN FOR USER: "
                                        + user.getId());
                    }

                    return notification;
                })
                .toList();

        System.out.println(
                ">>> NOTIFICATIONS READY TO SAVE: "
                        + notifications.size());

        if (notifications.isEmpty()) {
            System.out.println(">>> NO NOTIFICATIONS TO SAVE");
            return List.of();
        }

        List<Notification> saved =
                notificationRepository.saveAll(notifications);

        System.out.println(
                ">>> NOTIFICATIONS SAVED: " + saved.size());

        return saved;
    }
}