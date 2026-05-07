package com.hvac.workflow.repository;

import com.hvac.workflow.model.TechnicianRecord;
import org.springframework.data.jpa.repository.JpaRepository;

// Standard JPA repository for CRUD operations on technician records.
public interface TechnicianRepository extends JpaRepository<TechnicianRecord, String> {
}
