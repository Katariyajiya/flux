package com.flux.controller;

import com.flux.entity.Report;
import com.flux.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    @Value("${lambda.processing.secret}")
    private String lambdaProcessingSecret;
    private final SqsService sqsService;
    private final ReportService reportService;
    private final ReportWorkflowService reportWorkflowService;
    private final CsvReportParserService csvReportParserService;
    private final S3Service s3Service;

    public ReportController(SqsService sqsService, ReportService reportService,
                            ReportWorkflowService reportWorkflowService,
                            CsvReportParserService csvReportParserService, S3Service s3Service){
        this.sqsService = sqsService;
        this.reportService = reportService;
        this.reportWorkflowService = reportWorkflowService;
        this.csvReportParserService = csvReportParserService;
        this.s3Service = s3Service;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadReport(
            @RequestParam String reportId,
            @RequestParam("file") MultipartFile file) throws Exception {

        Report report = new Report();
        report.setReportId(reportId);
        report.setFileName(file.getOriginalFilename());

        Path tempFile = Files.createTempFile(
                "flux-report-",
                "-" + file.getOriginalFilename()
        );

        try {
            file.transferTo(tempFile.toFile());

            csvReportParserService.validate(tempFile.toString());

            String s3Key = "reports/"+reportId+"/"+file.getOriginalFilename();
            s3Service.uploadFile(tempFile,s3Key);
            report.setS3Key(s3Key);


            Report savedReport = reportService.createReport(report);

            String message = "ReportId="+savedReport.getReportId()+", S3Key="+savedReport.getS3Key();
            String messageId = sqsService.sendMessage(message);
            System.out.println("Report event sent to SQS. Message ID: " + messageId);


            return ResponseEntity.ok(
                    "Report uploaded successfully and sent for processing: "
                            + reportId
            );

        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    @PostMapping("/process")
    public ResponseEntity<String> processReport(
            @RequestParam String reportId,
            @RequestParam String s3Key,
            @RequestHeader("X-Lambda-Secret") String lambdaSecret) throws Exception {

        if (lambdaSecret == null || !lambdaSecret.equals(lambdaProcessingSecret)) {
            return ResponseEntity.status(403)
                    .body("Invalid Lambda secret");
        }

        Report report = reportService.findByReportId(reportId);

        Path tempFile = Files.createTempFile(
                "flux-process-",
                ".csv"
        );

        // AWS SDK getObject(Path) expects the destination file
        // to NOT already exist.
        Files.deleteIfExists(tempFile);

        try {
            s3Service.downloadFile(s3Key, tempFile);

            reportWorkflowService.processReport(
                    report,
                    tempFile.toString()
            );

            return ResponseEntity.ok(
                    "Report processed successfully: " + reportId
            );

        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

}
