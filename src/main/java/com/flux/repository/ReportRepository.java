package com.flux.repository;

import com.flux.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report,Long> {
    Optional<Report> findByReportId(String reportId);
    boolean existsByReportId(String reportId);
}
