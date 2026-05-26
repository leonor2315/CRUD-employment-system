package com.hvac.workflow.controller;

import com.hvac.workflow.model.TechnicianRecord;
import com.hvac.workflow.service.EmployeeWorkService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/director")
public class DirectorController {

    private final EmployeeWorkService workService;

    public DirectorController(EmployeeWorkService workService) {
        this.workService = workService;
    }

    @GetMapping("/employee-work")
    public List<TechnicianRecord> allEmployeeWork() {
        // Read-only employee directory for Managing Director access.
        return workService.getAll();
    }

    @GetMapping("/employee-work/search")
    public Page<TechnicianRecord> searchEmployeeWork(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String payrollStatus,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String jobGroup,
            Pageable pageable
    ) {
        return workService.search(false, q, status, payrollStatus, location, jobGroup, pageable);
    }

    @GetMapping("/employee-work/{employeeNo}")
    public TechnicianRecord employeeWorkById(@PathVariable String employeeNo) {
        return workService.getByEmployeeId(employeeNo);
    }
}
