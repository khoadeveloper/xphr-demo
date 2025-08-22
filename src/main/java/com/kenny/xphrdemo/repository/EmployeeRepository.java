package com.kenny.xphrdemo.repository;

import com.kenny.xphrdemo.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
