package com.flux.service;

import com.flux.entity.Report;
import com.flux.entity.User;
import com.flux.exception.ReportAlreadyExistsException;
import com.flux.repository.ReportRepository;
import com.flux.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReportService {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    public ReportService(ReportRepository reportRepository, UserRepository userRepository) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
    }
    public Report createReport(Report report) {

        if (reportRepository.existsByReportId(report.getReportId())) {
            throw new ReportAlreadyExistsException("Report already exists");
        }

        report.setStatus("UPLOADED");
        report.setUploadedAt(LocalDateTime.now());

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new IllegalArgumentException("Logged-in user not found"));
        report.setUploadedBy(user.getId());
        return reportRepository.save(report);
    }
}
