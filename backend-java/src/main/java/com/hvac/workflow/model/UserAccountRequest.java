package com.hvac.workflow.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserAccountRequest(
        @NotBlank @Size(max = 80) String username,
        @NotBlank @Size(max = 120) String displayName,
        @NotBlank @Pattern(regexp = "ADMIN|DIRECTOR|MANAGER|EMPLOYEE") String role,
        @Size(min = 8, max = 120)
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$", message = "Password must include letters and numbers")
        String password,
        Boolean enabled
) {
}
