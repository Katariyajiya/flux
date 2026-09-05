package com.flux.service;

import com.flux.entity.Department;
import com.flux.entity.Report;
import com.flux.entity.ReportReading;
import com.flux.repository.DepartmentRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvReportParserService {
    private final DepartmentRepository departmentRepository;

    public CsvReportParserService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public void validate(String filePath) throws IOException {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        try (Reader reader = Files.newBufferedReader(Path.of(filePath));
             CSVParser csvParser = CSVFormat.DEFAULT
                     .builder()
                     .setHeader()
                     .setSkipHeaderRecord(true)
                     .build()
                     .parse(reader)) {

            List<String> requiredColumns = List.of(
                    "reading_time",
                    "department",
                    "parameter",
                    "value",
                    "unit"
            );

            for (String column : requiredColumns) {
                if (!csvParser.getHeaderMap().containsKey(column)) {
                    throw new IllegalArgumentException(
                            "Invalid CSV format. Missing column: " + column
                    );
                }
            }

            for (CSVRecord record : csvParser) {

                String readingTimeValue = record.get("reading_time");
                String departmentName = record.get("department");
                String parameter = record.get("parameter");
                String value = record.get("value");
                String unit = record.get("unit");

                if (readingTimeValue.isBlank()
                        || departmentName.isBlank()
                        || parameter.isBlank()
                        || value.isBlank()
                        || unit.isBlank()) {
                    throw new IllegalArgumentException(
                            "Invalid CSV data. Required field is empty."
                    );
                }

                try {
                    LocalDateTime.parse(
                            readingTimeValue,
                            formatter
                    );
                } catch (Exception e) {
                    throw new IllegalArgumentException(
                            "Invalid reading_time format: "
                                    + readingTimeValue
                                    + ". Expected: yyyy-MM-dd HH:mm"
                    );
                }


                try {
                    Double.parseDouble(value);
                } catch (Exception e) {
                    throw new IllegalArgumentException(
                            "Invalid value: " + value
                    );
                }


                departmentRepository.findByName(departmentName)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Department not found: "
                                                + departmentName
                                )
                        );
            }
        }
    }

    public List<ReportReading> parse(String filePath, Report report) throws IOException{

        List<ReportReading> readings = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        try (Reader reader = Files.newBufferedReader(Path.of(filePath));
             CSVParser csvParser = CSVFormat.DEFAULT
                     .builder().setHeader()
                     .setSkipHeaderRecord(true)
                     .build().parse(reader)){
            for (CSVRecord record : csvParser){
                String readingTimeValue = record.get("reading_time");
                String departmentName = record.get("department");
                String parameter = record.get("parameter");
                Double value = Double.parseDouble(record.get("value"));
                String unit = record.get("unit");

                LocalDateTime readingTime =
                        LocalDateTime.parse(readingTimeValue, formatter);

                Department department =
                        departmentRepository.findByName(departmentName)
                                .orElseThrow(() ->
                                        new IllegalArgumentException(
                                                "Department not found: " + departmentName
                                        )
                                );

                ReportReading reading = new ReportReading();

                reading.setReport(report);
                reading.setDepartment(department);
                reading.setReadingTime(readingTime);
                reading.setParameter(parameter);
                reading.setValue(value);
                reading.setUnit(unit);

                readings.add(reading);
            }
        }
        return readings;
    }
}
