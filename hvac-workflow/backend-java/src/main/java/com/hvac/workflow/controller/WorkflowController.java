package com.hvac.workflow.controller;

import com.hvac.workflow.model.Customer;
import com.hvac.workflow.model.Job;
import com.hvac.workflow.model.JobRequest;
import com.hvac.workflow.service.WorkflowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class WorkflowController {
    private final WorkflowService workflowService;

    public WorkflowController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @GetMapping("/jobs")
    public List<Job> getJobs() {
        return workflowService.listJobs();
    }

    @GetMapping("/jobs/{jobId}")
    public Job getJob(@PathVariable String jobId) {
        return workflowService.getJob(jobId);
    }

    @PostMapping("/jobs")
    public ResponseEntity<Job> createJob(@Validated @RequestBody JobRequest request) {
        return new ResponseEntity<>(workflowService.createJob(request), HttpStatus.CREATED);
    }

    @GetMapping("/customers")
    public List<Customer> getCustomers() {
        return workflowService.listCustomers();
    }

    @PostMapping("/customers")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        return new ResponseEntity<>(workflowService.createCustomer(customer), HttpStatus.CREATED);
    }
}
