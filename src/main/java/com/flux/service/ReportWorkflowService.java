package com.flux.service;

import com.flux.entity.Department;
import com.flux.entity.Report;
import com.flux.entity.ReportReading;
import com.flux.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportWorkflowService {
    private final CsvReportParserService csvReportParserService;
    private final ReportProcessingService reportProcessingService;
    private final RecipientService recipientService;
    private final NotificationService notificationService;

    public ReportWorkflowService(CsvReportParserService csvReportParserService,
                                 ReportProcessingService reportProcessingService,
                                 RecipientService recipientService,
                                 NotificationService notificationService) {
        this.csvReportParserService = csvReportParserService;
        this.reportProcessingService = reportProcessingService;
        this.recipientService = recipientService;
        this.notificationService = notificationService;
    }

    public void processReport(Report report, String filePath) throws Exception{
        //Parse Csv
        List<ReportReading> readings = csvReportParserService.parse(filePath,report);
        //Save readings
        reportProcessingService.saveReadings(readings);
        //find departments represented in the report
        List<Department> departments = readings.stream()
                                        .map(ReportReading::getDepartment)
                                        .distinct()
                                        .toList();
        //create notifications for each department
        for (Department department : departments){
            List<User> recipients = recipientService.findRecipients(department);
            notificationService.createNotification(report,recipients,"Daily Report",
                    "A new daily report is available for your department.");
        }
    }
}
