package com.kenny.xphrdemo;

import com.kenny.xphrdemo.entity.Employee;
import com.kenny.xphrdemo.entity.Project;
import com.kenny.xphrdemo.entity.TimeRecord;
import com.kenny.xphrdemo.repository.EmployeeRepository;
import com.kenny.xphrdemo.repository.ProjectRepository;
import com.kenny.xphrdemo.repository.TimeRecordRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootTest
public class DataTests {

    @Autowired
    private TimeRecordRepository timeRecordRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * This test is used to populate data on table
     * to demonstrate the behaviour of the query
     * and not a real functional test case
     */
    @Test
    @Disabled
    public void populateData() {
        timeRecordRepository.deleteAll();
        projectRepository.deleteAll();
        employeeRepository.deleteAll();

        List<Project> projects = new ArrayList<>();

        for (int i = 1; i <= 100; i++) {
            Project project = new Project();
            project.setName("Project " + i);

            projects.add(project);
        }

        projects = projectRepository.saveAll(projects);

        List<Employee> employees = new ArrayList<>();

        for (int i = 1; i <= 500; i++) {
            Employee employee = new Employee();
            employee.setName("employee" + i);

            employees.add(employee);
        }

        employees = employeeRepository.saveAll(employees);

        List<TimeRecord> timeRecords = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            Collections.shuffle(employees);
            Collections.shuffle(projects);
            LocalDateTime fromDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(ThreadLocalRandom.current().nextInt()), ZoneId.systemDefault());
            LocalDateTime toDate = fromDate.plusMonths(3);

            TimeRecord record = new TimeRecord();
            record.setEmployeeId(employees.get(0).getId());
            record.setProjectId(projects.get(0).getId());
            record.setTimeFrom(fromDate);
            record.setTimeTo(toDate);

            timeRecords.add(record);

            if (timeRecords.size() > 500) {
                timeRecordRepository.saveAll(timeRecords);
                timeRecords.clear();
            }
        }

        if (!timeRecords.isEmpty()) {
            timeRecordRepository.saveAll(timeRecords);
        }
    }
}
