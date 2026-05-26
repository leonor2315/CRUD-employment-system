package com.hvac.workflow.controller;

import com.hvac.workflow.model.EmployeeAuditLog;
import com.hvac.workflow.service.AuditLogService;
import com.hvac.workflow.service.EmployeeWorkService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/audit")
public class AuditController {

    private final AuditLogService auditLogService;
    private final EmployeeWorkService employeeWorkService;

    public AuditController(AuditLogService auditLogService, EmployeeWorkService employeeWorkService) {
        this.auditLogService = auditLogService;
        this.employeeWorkService = employeeWorkService;
    }

    @GetMapping
    public List<EmployeeAuditLog> recentAudit() {
        return auditLogService.recent();
    }

    @GetMapping("/employee/{employeeNo}")
    public List<EmployeeAuditLog> auditForEmployee(@PathVariable String employeeNo) {
        return auditLogService.forEmployee(employeeNo);
    }

    @PostMapping("/export")
    public void recordExport(@RequestParam(defaultValue = "Employee CSV export") String details) {
        employeeWorkService.recordExport(details);
    }
}
