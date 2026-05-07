package com.hvac.workflow.model;

import jakarta.validation.constraints.NotBlank;

public record JobRequest(
        @NotBlank String id,
        @NotBlank String title,
        String description,
        @NotBlank String customerId,
        String assignedTechnician,
        @NotBlank String status,
        @NotBlank String scheduledDate,
        String serviceNotes
) {
}
