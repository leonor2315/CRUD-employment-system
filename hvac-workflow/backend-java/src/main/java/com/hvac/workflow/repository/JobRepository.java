package com.hvac.workflow.repository;

import com.hvac.workflow.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, String> {
}
