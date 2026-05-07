package com.hvac.workflow.controller;

import com.hvac.workflow.model.TechnicianRecord;
import com.hvac.workflow.model.TechnicianRequest;
import com.hvac.workflow.service.EmployeeWorkService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final EmployeeWorkService workService;

    public AdminController(EmployeeWorkService workService) {
        this.workService = workService;
    }

    @GetMapping("/employee-work")
    public List<TechnicianRecord> allEmployeeWork() {
        // Admin list endpoint for full employee records.
        return workService.getAll();
    }

    @GetMapping("/employee-work/{employeeNo}")
    public TechnicianRecord employeeWorkById(@PathVariable String employeeNo) {
        // Admin detail endpoint for one employee.
        return workService.getByEmployeeId(employeeNo);
    }

    @PostMapping("/employee-work")
    @ResponseStatus(HttpStatus.CREATED)
    public TechnicianRecord createEmployeeWork(@Valid @RequestBody TechnicianRequest request) {
        // Admin create endpoint for a new profile.
        return workService.create(request);
    }

    @PutMapping("/employee-work/{employeeNo}")
    public TechnicianRecord updateEmployeeWork(@PathVariable String employeeNo, @Valid @RequestBody TechnicianRequest request) {
        // Admin update endpoint for an existing profile.
        return workService.update(employeeNo, request);
    }

    @DeleteMapping("/employee-work/{employeeNo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployeeWork(@PathVariable String employeeNo) {
        // Admin delete endpoint.
        workService.delete(employeeNo);
    }
}
