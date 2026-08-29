package com.flux.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
public class Report{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String reportId;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String s3Key;
    @Column(nullable = false)
    private Long uploadedBy;
    @Column(nullable = false)
    private LocalDateTime uploadedAt;
//    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private String status = "UPLOADED";


    public Report() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getS3Key() {
        return s3Key;
    }

    public void setS3Key(String s3Key) {
        this.s3Key = s3Key;
    }

    public Long getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(Long uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

//    public ReportStatus getStatus() {
//        return status;
//    }
//
//    public void setStatus(ReportStatus status) {
//        this.status = status;
//    }

    public Report(
            Long id,
            String reportId,
            String fileName,
            String s3Key,
            Long uploadedBy,
            LocalDateTime uploadedAt,
            String status) {
        this.id = id;
        this.reportId = reportId;
        this.fileName = fileName;
        this.s3Key = s3Key;
        this.uploadedBy = uploadedBy;
        this.uploadedAt = uploadedAt;
        this.status = status;
    }
}
