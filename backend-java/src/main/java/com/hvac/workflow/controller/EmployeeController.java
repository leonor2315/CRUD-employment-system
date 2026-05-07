package com.hvac.workflow.controller;

import com.hvac.workflow.model.TechnicianRecord;
import com.hvac.workflow.service.EmployeeWorkService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employee")
@CrossOrigin(origins = "*")
public class EmployeeController {

    private final EmployeeWorkService workService;

    public EmployeeController(EmployeeWorkService workService) {
        this.workService = workService;
    }

    @GetMapping("/work/{employeeNo}")
    public TechnicianRecord ownWork(@PathVariable String employeeNo) {
        // Employee self-service endpoint (read-only profile fetch).
        return workService.getByEmployeeId(employeeNo);
    }
}
