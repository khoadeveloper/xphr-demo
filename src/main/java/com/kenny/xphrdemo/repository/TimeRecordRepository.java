package com.kenny.xphrdemo.repository;

import com.kenny.xphrdemo.entity.TimeRecord;
import com.kenny.xphrdemo.repository.projections.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TimeRecordRepository extends JpaRepository<TimeRecord, Long> {

    @Query("SELECT e.name AS user," +
            "      p.name AS project," +
            "      SUM((tr.timeTo - tr.timeFrom) BY HOUR) AS hours" +
            " FROM Employee e" +
            " JOIN TimeRecord tr ON e.id = tr.employeeId" +
            " JOIN Project p ON tr.projectId = p.id" +
            " WHERE tr.timeFrom BETWEEN :from AND :to" +
            " AND (:user IS NULL OR e.name = :user)" +
            " GROUP BY tr.employeeId, e.name, p.name" +
            " ORDER BY e.name, p.name")
    Page<Report> getReports(LocalDateTime from, LocalDateTime to, String user, Pageable pageable);
}
