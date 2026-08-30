package com.flux.service;

import com.flux.entity.Department;
import com.flux.entity.ReportReading;
import com.flux.entity.User;
import com.flux.repository.ReportReadingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportProcessingService {

    private final ReportReadingRepository reportReadingRepository;
    private final RecipientService recipientService;
    private final NotificationService notificationService;

    public ReportProcessingService(
            ReportReadingRepository reportReadingRepository,
            RecipientService recipientService,
            NotificationService notificationService) {
        this.reportReadingRepository = reportReadingRepository;
        this.recipientService = recipientService;
        this.notificationService = notificationService;
    }

    public List<ReportReading> saveReadings(
            List<ReportReading> readings) {

        List<ReportReading> savedReadings = reportReadingRepository.saveAll(readings);

        Map<Department,List<ReportReading>> readingByDepartment = savedReadings
                .stream().collect(Collectors.groupingBy(
                ReportReading::getDepartment
        ));

        for (Map.Entry<Department,List<ReportReading>> entry : readingByDepartment.entrySet()){
            Department department = entry.getKey();
            List<User> recipients = recipientService.findRecipients(department);
            if (recipients.isEmpty()) continue;

            ReportReading firstReading = entry.getValue().get(0);
            notificationService.createNotification(
                    firstReading.getReport(),
                    recipients,
                    "Daily Report",
                    "A new daily report is available for "+department.getName()+"."
            );
        }
        return savedReadings;
    }
}