<div align="center">

⚡ FLUX

Report Monitoring • Data Processing • Smart Notifications

A modern backend platform for turning operational reports into structured, department-aware insights and notifications.

<br/>






<br/>





</div>

✦ What is Flux?

Flux is a report monitoring and notification platform designed to transform operational report files into structured, department-specific data.

Instead of treating a report as just a file, Flux turns it into a workflow:

              ┌──────────────────────┐
              │    📄 REPORT FILE    │
              │       CSV / Data     │
              └──────────┬───────────┘
                         │
                         ▼
              ┌──────────────────────┐
              │   ⚙️  PROCESSING     │
              │ Parse & Validate     │
              └──────────┬───────────┘
                         │
                         ▼
              ┌──────────────────────┐
              │   🗄️  STRUCTURED     │
              │   REPORT READINGS    │
              └──────────┬───────────┘
                         │
                    Department
                     Matching
                         │
             ┌───────────┴───────────┐
             ▼                       ▼
      ┌──────────────┐       ┌──────────────┐
      │ 🏭 DEPARTMENT │       │ 👥 RECIPIENTS │
      │    DATA       │       │   MANAGER /   │
      └──────┬───────┘       │    WORKER     │
             │               └──────┬───────┘
             └──────────┬────────────┘
                        ▼
              ┌──────────────────────┐
              │ 🔔 NOTIFICATIONS     │
              │ Relevant users only  │
              └──────────────────────┘

One report in. Structured operational data out.
The right information reaches the right people.

✦ Why Flux?

Operational reports often contain valuable information, but raw files are difficult to monitor, search, process, and distribute.

Flux introduces a centralized pipeline for:

📥 Receiving report metadata

📊 Processing CSV readings

🏢 Mapping readings to departments

👤 Identifying relevant recipients

🔔 Creating department-specific notifications

🗃️ Persisting everything in PostgreSQL

The architecture is designed to grow into a complete web + mobile monitoring ecosystem.

✦ Core Capabilities

Capability

Description

📄 Report Management

Store and manage report metadata

📊 CSV Processing

Parse operational readings from CSV files

🏢 Department Mapping

Associate readings with departments

👥 Recipient Management

Find users based on department

🔔 Notifications

Create notifications for relevant users

🗄️ Persistence

Store reports, readings, users and notifications

🔐 Role Model

ADMIN, MANAGER and WORKER roles

📱 Future Mobile Support

Designed for push notification workflows

☁️ Future S3 Support

Designed for cloud report storage

✦ Architecture

Flux follows a clean layered backend architecture.

                         ┌─────────────────────┐
                         │       CLIENTS       │
                         │ Web • Mobile • API  │
                         └──────────┬──────────┘
                                    │
                                    ▼
                         ┌─────────────────────┐
                         │    CONTROLLERS      │
                         │    REST Endpoints   │
                         └──────────┬──────────┘
                                    │
                                    ▼
                         ┌─────────────────────┐
                         │      SERVICES       │
                         │ Business Logic      │
                         └──────────┬──────────┘
                                    │
                    ┌───────────────┼───────────────┐
                    ▼               ▼               ▼
             ┌────────────┐  ┌────────────┐  ┌────────────┐
             │   REPORT   │  │   CSV      │  │ NOTIFICATION│
             │ PROCESSING │  │ PROCESSOR  │  │   SERVICE   │
             └─────┬──────┘  └─────┬──────┘  └──────┬─────┘
                   │               │                │
                   └───────────────┼────────────────┘
                                   ▼
                         ┌─────────────────────┐
                         │    REPOSITORIES     │
                         │   Spring Data JPA   │
                         └──────────┬──────────┘
                                    │
                                    ▼
                         ┌─────────────────────┐
                         │     PostgreSQL      │
                         │       Database      │
                         └─────────────────────┘

✦ Data Flow

A typical report-processing workflow looks like this:

       REPORT
         │
         ▼
  ┌─────────────┐
  │ Report API  │
  └──────┬──────┘
         │
         ▼
  ┌─────────────┐
  │ CSV Parser  │
  └──────┬──────┘
         │
         ├───────────────┐
         ▼               ▼
   Reading Data      Department
         │             Lookup
         └───────┬───────┘
                 ▼
       ┌─────────────────┐
       │ Report Reading  │
       │     Storage     │
       └────────┬────────┘
                │
                ▼
       ┌─────────────────┐
       │ Find Recipients  │
       │ By Department    │
       └────────┬────────┘
                │
                ▼
       ┌─────────────────┐
       │  Notification   │
       │    Creation     │
       └────────┬────────┘
                │
                ▼
             🔔 USER

✦ Domain Model

┌──────────────────┐
│      REPORT      │
├──────────────────┤
│ id               │
│ reportId         │
│ fileName         │
│ s3Key            │
│ uploadedBy       │
│ uploadedAt       │
│ status           │
└────────┬─────────┘
         │
         │ 1 : N
         ▼
┌──────────────────┐        ┌──────────────────┐
│ REPORT_READING   │   N:1  │   DEPARTMENT     │
├──────────────────┤◄───────┤──────────────────┤
│ readingTime      │        │ id               │
│ parameter        │        │ name             │
│ value            │        └────────┬─────────┘
│ unit             │                 │
└──────────────────┘                 │ 1:N
                                     ▼
                            ┌──────────────────┐
                            │       USER       │
                            ├──────────────────┤
                            │ name             │
                            │ email            │
                            │ phone            │
                            │ fcmToken         │
                            │ role             │
                            └────────┬─────────┘
                                     │ 1:N
                                     ▼
                            ┌──────────────────┐
                            │  NOTIFICATION    │
                            ├──────────────────┤
                            │ report           │
                            │ user             │
                            │ title            │
                            │ message          │
                            │ status           │
                            └──────────────────┘

✦ Technology Stack

Backend

Java 21

Spring Boot

Spring Web

Spring Data JPA

Hibernate

Maven

Data Processing

Apache Commons CSV

Database

PostgreSQL

API Testing

Postman

psql

Planned Integrations

Firebase Cloud Messaging (FCM)

AWS S3

✦ Project Structure

flux/
│
├── src/
│   ├── main/
│   │   ├── java/com/flux/
│   │   │
│   │   ├── controller/
│   │   │   ├── ReportController.java
│   │   │   └── CsvTestController.java
│   │   │
│   │   ├── entity/
│   │   │   ├── Report.java
│   │   │   ├── ReportReading.java
│   │   │   ├── Department.java
│   │   │   ├── User.java
│   │   │   ├── Notification.java
│   │   │   └── Role.java
│   │   │
│   │   ├── repository/
│   │   │   ├── ReportRepository.java
│   │   │   ├── ReportReadingRepository.java
│   │   │   ├── DepartmentRepository.java
│   │   │   ├── UserRepository.java
│   │   │   └── NotificationRepository.java
│   │   │
│   │   └── service/
│   │       ├── ReportService.java
│   │       ├── ReportProcessingService.java
│   │       ├── CsvReportParserService.java
│   │       ├── RecipientService.java
│   │       └── NotificationService.java
│   │
│   └── resources/
│       └── application.properties
│
├── pom.xml
└── README.md

✦ CSV Input

Flux currently supports report data in a simple structured CSV format:

reading_time,department,parameter,value,unit
2026-08-30T08:00:00,DAM,water_level,82.5,m
2026-08-30T08:00:00,DAM,flow_rate,1200,m3/s
2026-08-30T08:00:00,ELECTRICAL,voltage,230,V

The parser extracts:

reading_time
department
parameter
value
unit

and converts each row into a persisted ReportReading.

✦ API

Create a Report

POST /api/reports

Example:

{
  "reportId": "RPT-001",
  "fileName": "daily-report.csv",
  "s3Key": "reports/RPT-001.csv",
  "uploadedBy": 1,
  "uploadedAt": "2026-08-30T20:50:00",
  "status": "UPLOADED"
}

Find Department Recipients

GET /test/csv/recipients/{departmentId}

Example:

GET /test/csv/recipients/1

Returns users belonging to the selected department.

Process a CSV Report

POST /test/csv/parse?filePath={filePath}&reportId={reportId}

Example:

POST /test/csv/parse?filePath=/path/to/daily-report.csv&reportId=RPT-001

Processing sequence:

Report ID
   ↓
Find Report
   ↓
Read CSV
   ↓
Resolve Departments
   ↓
Create ReportReadings
   ↓
Persist to PostgreSQL

Create Notifications

POST /test/csv/notifications/{reportId}/{departmentId}

Example:

POST /test/csv/notifications/RPT-001/1

The endpoint finds the department, identifies its users, and creates notifications for those recipients.

The /test/csv endpoints are currently development/testing endpoints and are expected to evolve into production APIs.

✦ Roles

Flux currently defines three user roles:

┌───────────┐
│   ADMIN   │
└─────┬─────┘
      │
      ├── System / user management
      │
      ▼
┌───────────┐
│  MANAGER  │
└─────┬─────┘
      │
      ├── Department monitoring
      │
      ▼
┌───────────┐
│   WORKER  │
└───────────┘
      │
      └── Relevant operational notifications

Authentication and authorization are planned for a later phase.

✦ Notification Vision

The notification architecture is designed around department-aware delivery.

                 REPORT
                    │
                    ▼
             REPORT PROCESSING
                    │
                    ▼
              DEPARTMENT
                    │
                    ▼
          ┌──────────────────┐
          │ Eligible Users   │
          └────────┬─────────┘
                   │
          ┌────────┴────────┐
          ▼                 ▼
       MANAGER            WORKER
          │                 │
          └────────┬────────┘
                   ▼
             NOTIFICATION
                   │
                   ▼
            ┌─────────────┐
            │     FCM     │
            │   (Future)  │
            └──────┬──────┘
                   ▼
              📱 DEVICE

✦ Future Platform

Flux is being designed beyond the current backend.

                    FLUX PLATFORM
                         │
        ┌────────────────┼────────────────┐
        │                │                │
        ▼                ▼                ▼
   WEB DASHBOARD     MOBILE APP       BACKEND API
        │                │                │
        └────────────────┼────────────────┘
                         │
                         ▼
                ┌─────────────────┐
                │   FLUX CORE     │
                ├─────────────────┤
                │ Reports         │
                │ Readings        │
                │ Departments     │
                │ Users           │
                │ Notifications   │
                └────────┬────────┘
                         │
              ┌──────────┴──────────┐
              ▼                     ▼
         PostgreSQL              AWS S3
                                  (Future)

Web Dashboard

Planned for administrators and managers:

Dashboard

Report history

Department monitoring

Reading charts

User management

Notification center

Profile and settings

Mobile Application

Planned for managers and workers:

Push notifications

Report details

Department readings

Notification history

Profile

Mobile-first monitoring

✦ Development Roadmap

Backend Foundation

Spring Boot setup

PostgreSQL integration

JPA entities

Repository layer

Report creation

Department management

User management

CSV parsing

Report reading persistence

Department-based recipients

Notification creation

Backend Improvements

Production report-processing APIs

CSV validation and error handling

Duplicate processing prevention

Duplicate notification prevention

Notification delivery tracking

Notification retry mechanism

Automated tests

Authentication

Role-based authorization

Integrations

Firebase Cloud Messaging

AWS S3

Email notifications

SMS notifications

Frontend

Web dashboard

Analytics and charts

Mobile application

Real-time monitoring

Deployment

Docker

CI/CD

Cloud deployment

Production monitoring

✦ Running Locally

1. Clone

git clone <your-repository-url>
cd flux

2. Configure PostgreSQL

Create the database:

CREATE DATABASE flux;

Configure your database connection in:

src/main/resources/application.properties

3. Build

mvn clean install

4. Run

mvn spring-boot:run

The application starts on:

http://localhost:8080

✦ Database Quick Checks

Connect to PostgreSQL:

psql -U postgres

Select Flux:

\c flux

List tables:

\dt

Check reports:

SELECT * FROM reports;

Check readings:

SELECT * FROM report_readings;

Check users:

SELECT * FROM users;

Check notifications:

SELECT * FROM notifications;

✦ Design Principles

Flux is being built around a few simple principles:

Structured over scattered

Convert raw reports into structured, queryable information.

Relevant over noisy

Notifications should reach the people who actually need them.

Modular over monolithic

Keep controllers, services, repositories and domain entities separated.

Scalable from the beginning

The backend is designed to support future web, mobile, cloud storage and notification integrations.

Simple first, powerful later

Build a reliable foundation before adding complex automation.

✦ Project Status

<div align="center">

🟣 Backend Foundation — ACTIVE DEVELOPMENT

Report ingestion, CSV processing, department mapping, reading persistence, recipient lookup and notification creation are currently implemented.

<br/>

Next major milestones

Production APIs → FCM → Authentication → Web Dashboard → Mobile App → Cloud Deployment

</div>

✦ Contributing

Flux is currently under active development.

If you want to contribute:

git checkout -b feature/your-feature

Make your changes, test them, and open a pull request.

For larger changes, discuss the architecture before implementation.

✦ License

License information will be added before the first public release.

<div align="center">

⚡ Flux

Monitor. Process. Understand. Notify.

Built to turn operational reports into actionable information.

<br/>

Spring Boot • PostgreSQL • Java 21 • Apache Commons CSV

</div>
