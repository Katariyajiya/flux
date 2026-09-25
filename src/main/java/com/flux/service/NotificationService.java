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

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> createNotification(
            Report report,
            List<User> recipients,
            String title,
            String message) {

        System.out.println(
                ">>> NOTIFICATION FLOW REACHED - REPORT: "
                        + report.getReportId()
        );

        System.out.println(
                ">>> RECIPIENT COUNT: "
                        + recipients.size()
        );

        List<Notification> notifications = recipients.stream()
                .filter(user -> {
                    boolean exists =
                            notificationRepository.existsByReportAndUser(
                                    report,
                                    user
                            );

                    System.out.println(
                            ">>> USER "
                                    + user.getId()
                                    + " / "
                                    + user.getEmail()
                                    + " | EXISTS: "
                                    + exists
                    );

                    return !exists;
                })
                .map(user -> {

                    System.out.println(
                            ">>> CREATING NOTIFICATION FOR USER: "
                                    + user.getId()
                                    + " / "
                                    + user.getEmail()
                    );

                    Notification notification = new Notification();

                    notification.setReport(report);
                    notification.setUser(user);
                    notification.setTitle(title);
                    notification.setMessage(message);
                    notification.setStatus("PENDING");
                    notification.setCreatedAt(LocalDateTime.now());

                    return notification;
                })
                .toList();

        System.out.println(
                ">>> NOTIFICATIONS READY TO SAVE: "
                        + notifications.size()
        );

        if (notifications.isEmpty()) {
            System.out.println(
                    ">>> NO NOTIFICATIONS TO SAVE"
            );

            return List.of();
        }

        List<Notification> saved =
                notificationRepository.saveAll(notifications);

        System.out.println(
                ">>> NOTIFICATIONS SAVED: "
                        + saved.size()
        );

        return saved;
    }
}



























//package com.flux.service;
//
//import com.flux.entity.Notification;
//import com.flux.entity.Report;
//import com.flux.entity.User;
//import com.flux.repository.NotificationRepository;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//@Service
//public class NotificationService {
//    private final NotificationRepository notificationRepository;
//    private final RecipientService recipientService;
//    private final FcmNotificationService fcmNotificationService;
//
//    public NotificationService(NotificationRepository notificationRepository, RecipientService recipientService, FcmNotificationService fcmNotificationService) {
//        this.notificationRepository = notificationRepository;
//        this.recipientService = recipientService;
//        this.fcmNotificationService = fcmNotificationService;
//    }
//
//    public List<Notification> createNotification(
//            Report report,
//            List<User> recipients,
//            String title,
//            String message) {
//
//        List<Notification> notifications = recipients.stream()
//                .filter(user ->
//                        !notificationRepository.existsByReportAndUser(report, user))
//                .map(user -> {
//
//                    Notification notification = new Notification();
//
//                    notification.setReport(report);
//                    notification.setUser(user);
//                    notification.setTitle(title);
//                    notification.setMessage(message);
//                    notification.setStatus("PENDING");
//                    notification.setCreatedAt(LocalDateTime.now());
//
//                    if (user.getFcmToken() != null &&
//                            !user.getFcmToken().isBlank()) {
//
//                        try {
//                            fcmNotificationService.sendNotification(
//                                    user.getFcmToken(),
//                                    title,
//                                    message
//                            );
//
//                            notification.setStatus("SENT");
//
//                        } catch (Exception e) {
//
//                            notification.setStatus("FAILED");
//
//                            System.out.println(
//                                    "FCM failed for user "
//                                            + user.getId()
//                                            + ": "
//                                            + e.getMessage()
//                            );
//                        }
//                    }
//
//                    return notification;
//                })
//                .toList();
//
//        if (notifications.isEmpty()) {
//            return List.of();
//        }
//
//        return notificationRepository.saveAll(notifications);
//    }
//}
