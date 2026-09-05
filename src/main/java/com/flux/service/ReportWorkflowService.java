package com.flux.service;

import com.flux.entity.Department;
import com.flux.entity.Report;
import com.flux.entity.ReportReading;
import com.flux.entity.User;
import com.flux.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportWorkflowService {
    private final CsvReportParserService csvReportParserService;
    private final ReportProcessingService reportProcessingService;
    private final RecipientService recipientService;
    private final NotificationService notificationService;
    private final ReportRepository reportRepository;

    public ReportWorkflowService(CsvReportParserService csvReportParserService,
                                 ReportProcessingService reportProcessingService,
                                 RecipientService recipientService,
                                 NotificationService notificationService,
                                 ReportRepository reportRepository) {
        this.csvReportParserService = csvReportParserService;
        this.reportProcessingService = reportProcessingService;
        this.recipientService = recipientService;
        this.notificationService = notificationService;
        this.reportRepository = reportRepository;
    }

    public void processReport(Report report, String filePath) throws Exception {

        try {
            report.setStatus("PROCESSING");
            reportRepository.save(report);


            List<ReportReading> readings =
                    csvReportParserService.parse(filePath, report);


            reportProcessingService.saveReadings(readings);


            List<Department> departments = readings.stream()
                    .map(ReportReading::getDepartment)
                    .distinct()
                    .toList();


            for (Department department : departments) {

                List<User> recipients =
                        recipientService.findRecipients(department);

                notificationService.createNotification(
                        report,
                        recipients,
                        "Daily Report",
                        "A new daily report is available for your department."
                );
            }


            report.setStatus("PROCESSED");
            reportRepository.save(report);

        } catch (Exception e) {


            report.setStatus("FAILED");
            reportRepository.save(report);

            throw e;
        }
    }

}
