package com.flux.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "report_readings")
public class ReportReading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id",nullable = false)
    private Report report;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id",nullable = false)
    private Department department;

    @Column(nullable = false)
    private LocalDateTime readingTime;

    @Column(nullable = false)
    private String parameter;

    @Column(nullable = false)
    private Double value;

    @Column(nullable = false)
    private String unit;

    public ReportReading() {
    }

    public ReportReading(
            Long id,
            Report report,
            Department department,
            LocalDateTime readingTime,
            String parameter,
            Double value,
            String unit) {
        this.id = id;
        this.report = report;
        this.department = department;
        this.readingTime = readingTime;
        this.parameter = parameter;
        this.value = value;
        this.unit = unit;
    }

    public Long getId() {
        return id;
    }

    public Report getReport() {
        return report;
    }

    public Department getDepartment() {
        return department;
    }

    public LocalDateTime getReadingTime() {
        return readingTime;
    }

    public String getParameter() {
        return parameter;
    }

    public Double getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }
}
