package com.kenny.xphrdemo.repository;

import com.kenny.xphrdemo.entity.TimeRecord;
import com.kenny.xphrdemo.repository.projections.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeRecordRepository extends JpaRepository<TimeRecord, Long> {
    @Query("SELECT e.name AS user," +
            "      p.name AS project," +
            "      SUM((tr.timeTo - tr.timeFrom) BY HOUR) AS hours" +
            " FROM Employee e" +
            " JOIN TimeRecord tr ON e.id = tr.employeeId" +
            " JOIN Project p ON tr.projectId = p.id" +
            " WHERE 1 = 1" +
            " AND (:user IS NULL OR e.name = :user)" +
            " GROUP BY tr.employeeId, e.name, p.name" +
            " ORDER BY e.name, p.name")
    List<Report> getReports(String user);
}
