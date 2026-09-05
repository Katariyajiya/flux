package com.flux.controller;

import com.flux.entity.Report;
import com.flux.service.CsvReportParserService;
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
    private final CsvReportParserService csvReportParserService;

    public ReportController(ReportService reportService,
                            ReportWorkflowService reportWorkflowService,
                            CsvReportParserService csvReportParserService){
        this.reportService = reportService;
        this.reportWorkflowService = reportWorkflowService;
        this.csvReportParserService = csvReportParserService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadReport(
            @RequestParam String reportId,
            @RequestParam("file") MultipartFile file) throws Exception {

        Report report = new Report();
        report.setReportId(reportId);
        report.setFileName(file.getOriginalFilename());
        report.setS3Key("temporary/" + file.getOriginalFilename());


        Path tempFile = Files.createTempFile(
                "flux-report-",
                "-" + file.getOriginalFilename()
        );

        try {
            file.transferTo(tempFile.toFile());


            csvReportParserService.validate(tempFile.toString());


            Report savedReport = reportService.createReport(report);


            reportWorkflowService.processReport(
                    savedReport,
                    tempFile.toString()
            );

            return ResponseEntity.ok(
                    "Report uploaded and processed successfully: "
                            + reportId
            );

        } finally {
            Files.deleteIfExists(tempFile);
        }
    }
}
