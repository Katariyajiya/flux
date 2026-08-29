package com.flux.service;

import com.flux.entity.Report;
import com.flux.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReportService {
    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }
    public Report createReport(Report report) {

        if (reportRepository.existsByReportId(report.getReportId())) {
            throw new IllegalArgumentException("Report already exists");
        }

        report.setStatus("UPLOADED");
        report.setUploadedAt(LocalDateTime.now());
        report.setUploadedBy(1L);

        System.out.println("STATUS: " + report.getStatus());
        System.out.println("UPLOADED BY: " + report.getUploadedBy());
        System.out.println("UPLOADED AT: " + report.getUploadedAt());

        return reportRepository.save(report);
    }
}
