package com.hvac.workflow.service;

import com.hvac.workflow.model.EmployeeAuditLog;
import com.hvac.workflow.repository.EmployeeAuditLogRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class AuditLogService {

    private final EmployeeAuditLogRepository repository;

    public AuditLogService(EmployeeAuditLogRepository repository) {
        this.repository = repository;
    }

    public EmployeeAuditLog record(String employeeNo, String action, String details) {
        EmployeeAuditLog log = new EmployeeAuditLog();
        log.setEmployeeNo(employeeNo);
        log.setAction(action);
        log.setActor(currentActor());
        log.setOccurredAt(Instant.now());
        log.setDetails(details);
        return repository.save(log);
    }

    public List<EmployeeAuditLog> recent() {
        return repository.findTop100ByOrderByOccurredAtDesc();
    }

    public List<EmployeeAuditLog> forEmployee(String employeeNo) {
        return repository.findByEmployeeNoOrderByOccurredAtDesc(employeeNo);
    }

    private String currentActor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication == null || authentication.getName() == null ? "system" : authentication.getName();
    }
}
