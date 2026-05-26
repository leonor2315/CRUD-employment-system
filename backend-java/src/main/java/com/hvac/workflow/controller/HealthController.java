package com.hvac.workflow.controller;

import com.hvac.workflow.repository.EmployeeAuditLogRepository;
import com.hvac.workflow.repository.TechnicianRepository;
import com.hvac.workflow.repository.UserAccountRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class HealthController {

    private final TechnicianRepository technicianRepository;
    private final UserAccountRepository userAccountRepository;
    private final EmployeeAuditLogRepository auditLogRepository;

    public HealthController(
            TechnicianRepository technicianRepository,
            UserAccountRepository userAccountRepository,
            EmployeeAuditLogRepository auditLogRepository
    ) {
        this.technicianRepository = technicianRepository;
        this.userAccountRepository = userAccountRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        // Simple liveness probe used by Docker/orchestrators.
        return Map.of("status", "ok");
    }

    @GetMapping("/api/admin/system/status")
    public Map<String, Object> systemStatus() {
        Map<String, Object> status = new LinkedHashMap<>();
        status.put("status", "ok");
        status.put("activeEmployees", technicianRepository.findByArchivedFalse().size());
        status.put("archivedEmployees", technicianRepository.findByArchivedTrue().size());
        status.put("userAccounts", userAccountRepository.count());
        status.put("auditEvents", auditLogRepository.count());
        return status;
    }
}
