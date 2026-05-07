package com.hvac.workflow.config;

import com.hvac.workflow.model.Customer;
import com.hvac.workflow.model.Job;
import com.hvac.workflow.repository.CustomerRepository;
import com.hvac.workflow.repository.JobRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class DataInitializer {
    @Bean
    public CommandLineRunner seedDatabase(JobRepository jobRepository, CustomerRepository customerRepository) {
        return args -> {
            if (customerRepository.count() == 0) {
                customerRepository.save(new Customer("CUST-100", "Southside HVAC", "302 Main St", "555-0123", "contact@southsidehvac.com"));
                customerRepository.save(new Customer("CUST-101", "Riverview Properties", "1209 River Rd", "555-0456", "office@riverviewprop.com"));
            }
            if (jobRepository.count() == 0) {
                jobRepository.save(new Job("JOB-001", "AC maintenance", "Quarterly AC tune-up for rooftop unit", "CUST-100", "Alex Rivera", "Scheduled", "2026-05-10", "Bring filter replacement kit", LocalDateTime.now()));
                jobRepository.save(new Job("JOB-002", "Heat pump inspection", "Pre-winter heat pump service", "CUST-101", "Maya Chen", "In Progress", "2026-05-12", "Confirm thermostat compatibility", LocalDateTime.now()));
            }
        };
    }
}
