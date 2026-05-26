package com.hvac.workflow.controller;

import com.hvac.workflow.model.TechnicianRecord;
import com.hvac.workflow.repository.TechnicianRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final TechnicianRepository technicianRepository;

    public AnalyticsController(TechnicianRepository technicianRepository) {
        this.technicianRepository = technicianRepository;
    }

    @GetMapping("/work-summary")
    public Map<String, Object> workSummary() {
        List<TechnicianRecord> activeEmployees = technicianRepository.findByArchivedFalse();

        long engagedEmployees = activeEmployees.stream()
                .filter(employee -> normalized(employee.getStatus()).equals("engaged"))
                .count();
        long onPayrollEmployees = activeEmployees.stream()
                .filter(employee -> normalized(employee.getPayrollStatus()).equals("on payroll"))
                .count();

        Map<String, Long> statusBreakdown = groupByValue(activeEmployees, "status");
        Map<String, Long> locationBreakdown = groupByValue(activeEmployees, "location");

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("totalEmployees", activeEmployees.size());
        summary.put("activeEmployees", engagedEmployees);
        summary.put("onPayrollEmployees", onPayrollEmployees);
        summary.put("statusBreakdown", statusBreakdown);
        summary.put("locationBreakdown", locationBreakdown);
        return summary;
    }

    private Map<String, Long> groupByValue(List<TechnicianRecord> employees, String field) {
        return employees.stream()
                .collect(Collectors.groupingBy(
                        employee -> switch (field) {
                            case "status" -> displayValue(employee.getStatus());
                            case "location" -> displayValue(employee.getLocation());
                            default -> "Unknown";
                        },
                        LinkedHashMap::new,
                        Collectors.counting()
                ));
    }

    private String normalized(String value) {
        return String.valueOf(value == null ? "" : value).trim().toLowerCase();
    }

    private String displayValue(String value) {
        String clean = String.valueOf(value == null ? "" : value).trim();
        return clean.isBlank() ? "Unknown" : clean;
    }
}
