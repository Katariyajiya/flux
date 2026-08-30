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
