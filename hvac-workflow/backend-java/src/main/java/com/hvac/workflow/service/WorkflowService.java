package com.hvac.workflow.service;

import com.hvac.workflow.model.Customer;
import com.hvac.workflow.model.Job;
import com.hvac.workflow.model.JobRequest;
import com.hvac.workflow.repository.CustomerRepository;
import com.hvac.workflow.repository.JobRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class WorkflowService {
    private final JobRepository jobRepository;
    private final CustomerRepository customerRepository;

    public WorkflowService(JobRepository jobRepository, CustomerRepository customerRepository) {
        this.jobRepository = jobRepository;
        this.customerRepository = customerRepository;
    }

    public List<Job> listJobs() {
        return jobRepository.findAll();
    }

    public Job getJob(String id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Job not found"));
    }

    public Job createJob(JobRequest request) {
        if (jobRepository.existsById(request.id())) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.CONFLICT, "Job ID already exists");
        }
        if (!customerRepository.existsById(request.customerId())) {
            throw new ResponseStatusException(NOT_FOUND, "Customer not found");
        }
        Job job = new Job(request.id(), request.title(), request.description(), request.customerId(), request.assignedTechnician(), request.status(), request.scheduledDate(), request.serviceNotes(), LocalDateTime.now());
        return jobRepository.save(job);
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public List<Customer> listCustomers() {
        return customerRepository.findAll();
    }
}
