package com.kenny.xphrdemo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(schema = "xphr", name = "time_record")
@Getter
@Setter
public class TimeRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id")
    @Basic
    private Long employeeId;

    @Column(name = "project_id")
    @Basic
    private Long projectId;

    @Column(name = "time_from")
    @Basic
    private LocalDateTime timeFrom;

    @Column(name = "time_to")
    @Basic
    private LocalDateTime timeTo;
}
