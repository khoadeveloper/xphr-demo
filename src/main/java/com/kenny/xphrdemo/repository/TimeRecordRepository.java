package com.kenny.xphrdemo.repository;

import com.kenny.xphrdemo.entity.TimeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeRecordRepository extends JpaRepository<TimeRecord, Long> {
}
