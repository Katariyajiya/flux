package com.flux.controller;

import com.flux.entity.Report;
import com.flux.service.ReportService;
import com.flux.service.ReportWorkflowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService reportService;
    private final ReportWorkflowService reportWorkflowService;

    public ReportController(ReportService reportService, ReportWorkflowService reportWorkflowService){
        this.reportService = reportService;
        this.reportWorkflowService = reportWorkflowService;
    }

    @PostMapping
    public ResponseEntity<Report> createReport(@RequestBody Report report){
        Report savedReport = reportService.createReport(report);
        return ResponseEntity.ok(savedReport);
    }
    @PostMapping("/upload")
    public ResponseEntity<String> uploadReport(
            @RequestParam String reportId,
            @RequestParam("file") MultipartFile file) throws Exception {

        // Create report metadata
        Report report = new Report();
        report.setReportId(reportId);
        report.setFileName(file.getOriginalFilename());
        report.setS3Key("temporary/" + file.getOriginalFilename());

        Report savedReport = reportService.createReport(report);

        // Save uploaded file temporarily
        Path tempFile = Files.createTempFile(
                "flux-report-",
                "-" + file.getOriginalFilename()
        );

        file.transferTo(tempFile.toFile());

        // Process complete report
        reportWorkflowService.processReport(
                savedReport,
                tempFile.toString()
        );

        return ResponseEntity.ok(
                "Report uploaded and processed successfully: "
                        + reportId
        );
    }
}
