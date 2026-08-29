package com.flux.repository;

import com.flux.entity.Department;
import com.flux.entity.Report;
import com.flux.entity.ReportReading;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportReadingRepository extends JpaRepository<ReportReading,Long> {
    List<ReportReading> findByReport(Report report);
    List<ReportReading> findByDepartment(Department department);
    List<ReportReading> findByReportAndDepartment(Report report,Department department);
}
