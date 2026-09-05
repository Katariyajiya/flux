package com.flux.service;

import com.flux.entity.Report;
import com.flux.exception.ReportAlreadyExistsException;
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
            throw new ReportAlreadyExistsException("Report already exists");
        }

        report.setStatus("UPLOADED");
        report.setUploadedAt(LocalDateTime.now());
        report.setUploadedBy(1L);

        return reportRepository.save(report);
    }
}
