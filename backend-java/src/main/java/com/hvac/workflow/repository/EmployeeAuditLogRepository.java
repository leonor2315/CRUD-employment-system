package com.hvac.workflow.repository;

import com.hvac.workflow.model.EmployeeAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeAuditLogRepository extends JpaRepository<EmployeeAuditLog, Long> {
    List<EmployeeAuditLog> findTop100ByOrderByOccurredAtDesc();
    List<EmployeeAuditLog> findByEmployeeNoOrderByOccurredAtDesc(String employeeNo);
}
