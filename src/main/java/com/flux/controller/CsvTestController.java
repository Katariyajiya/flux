package com.flux.controller;

import com.flux.entity.*;
import com.flux.repository.DepartmentRepository;
import com.flux.repository.ReportRepository;
import com.flux.service.CsvReportParserService;
import com.flux.service.NotificationService;
import com.flux.service.RecipientService;
import com.flux.service.ReportProcessingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/test/csv")
public class CsvTestController {

    private final CsvReportParserService csvReportParserService;
    private final ReportProcessingService reportProcessingService;
    private final ReportRepository reportRepository;
    private final RecipientService recipientService;
    private final DepartmentRepository departmentRepository;
    private final NotificationService notificationService;


    public CsvTestController(CsvReportParserService csvReportParserService,
                             ReportProcessingService reportProcessingService, ReportRepository reportRepository, RecipientService recipientService,
                             DepartmentRepository departmentRepository, NotificationService notificationService) {
        this.csvReportParserService = csvReportParserService;
        this.reportProcessingService = reportProcessingService;
        this.reportRepository = reportRepository;
        this.recipientService = recipientService;
        this.departmentRepository = departmentRepository;
        this.notificationService = notificationService;
    }


    @GetMapping("/recipients/{departmentId}")
    public List<User> getRecipients(@PathVariable Long departmentId){
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(()->new IllegalArgumentException("Department not found: "+departmentId));
         return recipientService.findRecipients(department);
    }

    @PostMapping("/parse")
    public List<ReportReading> parseCsv(
            @RequestParam String filePath
    ) throws Exception {

        // Temporary report for testing the parser
        Report report = reportRepository.findByReportId("RPT-001")
                .orElseThrow(() ->
                        new IllegalArgumentException("Report RPT-001 not found"));

        List<ReportReading> readings =
                csvReportParserService.parse(filePath, report);

        return reportProcessingService.saveReadings(readings);
    }

    @PostMapping("/notifications/{reportId}/{departmentId}")
    public List<Notification> createNotifications(@PathVariable String reportId,@PathVariable Long departmentId){
        Report report = reportRepository.findByReportId(reportId)
                .orElseThrow(()-> new IllegalArgumentException("Report not found: "+reportId));

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(()-> new IllegalArgumentException("Department not found: "+departmentId));

        List<User> recipients = recipientService.findRecipients(department);

        return notificationService.createNotification(report,recipients,"Daily Report",
                "A new daily report is available for your department.");

    }
}