package com.hvac.workflow.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
public class Job {
    @Id
    private String id;
    private String title;
    private String description;
    private String customerId;
    private String assignedTechnician;
    private String status;
    private String scheduledDate;
    @Column(columnDefinition = "TEXT")
    private String serviceNotes;
    private LocalDateTime createdAt;

    public Job() {
    }

    public Job(String id, String title, String description, String customerId, String assignedTechnician, String status, String scheduledDate, String serviceNotes, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.customerId = customerId;
        this.assignedTechnician = assignedTechnician;
        this.status = status;
        this.scheduledDate = scheduledDate;
        this.serviceNotes = serviceNotes;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAssignedTechnician() {
        return assignedTechnician;
    }

    public void setAssignedTechnician(String assignedTechnician) {
        this.assignedTechnician = assignedTechnician;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(String scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getServiceNotes() {
        return serviceNotes;
    }

    public void setServiceNotes(String serviceNotes) {
        this.serviceNotes = serviceNotes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
