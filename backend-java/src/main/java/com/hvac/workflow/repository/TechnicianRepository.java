package com.hvac.workflow.repository;

import com.hvac.workflow.model.TechnicianRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

// Standard JPA repository for CRUD operations on technician records.
public interface TechnicianRepository extends JpaRepository<TechnicianRecord, String> {
    @Query("select t from TechnicianRecord t where coalesce(t.archived, false) = false")
    List<TechnicianRecord> findByArchivedFalse();

    @Query("select t from TechnicianRecord t where coalesce(t.archived, false) = true")
    List<TechnicianRecord> findByArchivedTrue();

    @Query("""
            select t from TechnicianRecord t
            where coalesce(t.archived, false) = :archived
              and (:q is null or :q = ''
                   or lower(coalesce(t.employeeNo, '')) like lower(concat('%', :q, '%'))
                   or lower(coalesce(t.fullName, '')) like lower(concat('%', :q, '%'))
                   or lower(coalesce(t.jobTitle, '')) like lower(concat('%', :q, '%'))
                   or lower(coalesce(t.jobGroup, '')) like lower(concat('%', :q, '%'))
                   or lower(coalesce(t.location, '')) like lower(concat('%', :q, '%'))
                   or lower(coalesce(t.emailAddress, '')) like lower(concat('%', :q, '%')))
              and (:status is null or :status = '' or lower(coalesce(t.status, '')) = lower(:status))
              and (:payrollStatus is null or :payrollStatus = '' or lower(coalesce(t.payrollStatus, '')) = lower(:payrollStatus))
              and (:location is null or :location = '' or lower(coalesce(t.location, '')) = lower(:location))
              and (:jobGroup is null or :jobGroup = '' or lower(coalesce(t.jobGroup, '')) = lower(:jobGroup))
            """)
    Page<TechnicianRecord> search(
            boolean archived,
            String q,
            String status,
            String payrollStatus,
            String location,
            String jobGroup,
            Pageable pageable
    );
}
