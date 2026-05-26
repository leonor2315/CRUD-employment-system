package com.hvac.workflow.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.Map;

// Request DTO received from frontend create/update technician forms.
public record TechnicianRequest(
        @NotBlank @Size(max = 40) String employeeNo,
        @Size(max = 20) String title,
        @NotBlank @Size(max = 160) String fullName,
        @NotBlank @Size(max = 80) String firstName,
        @Size(max = 80) String middleName,
        @NotBlank @Size(max = 80) String lastName,
        @Size(max = 20) String gender,
        String dateOfBirth,
        @Min(0) Integer age,
        @Size(max = 40) String maritalStatus,
        @Size(max = 120) String jobTitle,
        @Size(max = 80) String category,
        @Size(max = 80) String payrollStatus,
        @PositiveOrZero Double amount,
        @Size(max = 120) String grading,
        @Size(max = 120) String jobGroup,
        @Size(max = 120) String location,
        String employmentDate,
        @Size(max = 255) String entryChecklist,
        @Size(max = 80) String status,
        String exitDate,
        @Size(max = 80) String lengthService,
        String exitChecklist,
        @Min(0) Integer annualLeaveEntitlement,
        @Min(0) Integer leaveTaken,
        Integer leaveBalance,
        @Size(max = 80) String socialSecurityNo,
        @Size(max = 120) String bankName,
        @Size(max = 120) String bankBranchName,
        @Size(max = 80) String accountNo,
        @Size(max = 80) String taxIdentificationNo,
        @Size(max = 80) String ghanaCardNumber,
        Boolean providentFund,
        @Size(max = 120) String vehicleAndFuel,
        @PositiveOrZero Double transportAllowanceGallons,
        Boolean cellPhone,
        @PositiveOrZero Double airtimeAmount,
        Boolean lunch,
        @Size(max = 80) String medicalPlan,
        @Size(max = 120) String spouseOrNa,
        @Min(0) Integer childrenCount,
        @Size(max = 60) String mobilePhoneNo,
        @Size(max = 60) String businessPhoneNo,
        @Size(max = 60) String homePhoneNo,
        @Email @Size(max = 160) String emailAddress,
        @Size(max = 255) String postalAddress,
        String residentialAddress,
        @Size(max = 160) String emergencyContact,
        @Size(max = 60) String emergencyPhoneNo,
        @Size(max = 80) String relationship,
        @Size(max = 160) String spouseName,
        String namesOfChildren,
        @Min(0) Integer numberOfDependents,
        @Size(max = 160) String nextOfKinName,
        String nextOfKinContactDetails,
        @Size(max = 160) String guarantorName,
        @Size(max = 60) String guarantorContact,
        String guarantorResidentialAddress,
        @Size(max = 80) String guarantorGhanaCard,
        Map<String, String> customFields
) {
}
