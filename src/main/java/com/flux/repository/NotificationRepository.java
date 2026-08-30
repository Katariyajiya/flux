package com.flux.repository;

import com.flux.entity.Notification;
import com.flux.entity.Report;
import com.flux.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {
    List<Notification> findByReport(Report report);
    List<Notification> findByUser(User user);
    boolean existsByReportAndUser(Report report, User user);
}