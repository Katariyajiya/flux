Project Flow
                
                ┌─────────────────────┐
                │   Create Report     │
                │    RPT-004 etc.     │
                └──────────┬──────────┘
                           │
                           ▼
                ┌─────────────────────┐
                │   CSV File          │
                │ daily-report.csv    │
                └──────────┬──────────┘
                           │
                           ▼
                ┌─────────────────────┐
                │ CsvReportParser      │
                │ Service              │
                └──────────┬──────────┘
                           │
                 Reads every CSV row
                           │
                           ▼
                ┌─────────────────────┐
                │   ReportReading     │
                │                     │
                │ reading_time        │
                │ department         │
                │ parameter           │
                │ value               │
                │ unit                │
                └──────────┬──────────┘
                           │
                           ▼
                ┌─────────────────────┐
                │ ReportProcessing    │
                │ Service             │
                │                     │
                │ saveAll(readings)   │
                └──────────┬──────────┘
                           │
                           ▼
                 Saved in PostgreSQL
                           │
                           ▼
          ┌────────────────────────────────┐
          │ Notification Request           │
          │ /notifications/RPT-004/1       │
          └────────────────┬───────────────┘
                           │
                           ▼
                ┌─────────────────────┐
                │ RecipientService    │
                │                     │
                │ Department ID = 1   │
                └──────────┬──────────┘
                           │
                     Finds users
                           │
                ┌──────────┴──────────┐
                ▼                     ▼
              Raj                   Aman
              DAM                   DAM
                │                     │
                └──────────┬──────────┘
                           ▼
                ┌─────────────────────┐
                │ NotificationService │
                └──────────┬──────────┘
                           │
                 Check duplicate:
             RPT-004 + User already?
                     /          \
                   YES          NO
                    │            │
                    ▼            ▼
               Skip user      Create
                              notification
                                  │
                                  ▼
                          Send FCM notification
                                  │
                          ┌───────┴───────┐
                          ▼               ▼
                        SENT            FAILED
                          │               │
                          └───────┬───────┘
                                  ▼
                         Save notification
                         in PostgreSQL
