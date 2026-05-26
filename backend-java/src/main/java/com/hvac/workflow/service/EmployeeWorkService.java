package com.hvac.workflow.service;

import com.hvac.workflow.model.TechnicianRecord;
import com.hvac.workflow.model.TechnicianRequest;
import com.hvac.workflow.repository.TechnicianRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeWorkService {

    private final TechnicianRepository repository;
    private final ObjectMapper objectMapper;
    private final AuditLogService auditLogService;

    public EmployeeWorkService(
            TechnicianRepository repository,
            ObjectMapper objectMapper,
            AuditLogService auditLogService
    ) {
        this.repository = repository;
        this.objectMapper = objectMapper;
        this.auditLogService = auditLogService;
    }

    public List<TechnicianRecord> getAll() {
        // Return active employee records for dashboard/list views.
        return repository.findByArchivedFalse();
    }

    public List<TechnicianRecord> getArchived() {
        return repository.findByArchivedTrue();
    }

    public Page<TechnicianRecord> search(
            boolean archived,
            String q,
            String status,
            String payrollStatus,
            String location,
            String jobGroup,
            Pageable pageable
    ) {
        return repository.search(archived, q, status, payrollStatus, location, jobGroup, pageable);
    }

    public TechnicianRecord getByEmployeeId(String employeeId) {
        // Fetch one record or return 404 when employee number does not exist.
        TechnicianRecord record = repository.findById(employeeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee record not found"));
        if (Boolean.TRUE.equals(record.getArchived())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee record is archived");
        }
        return record;
    }

    public TechnicianRecord create(TechnicianRequest request) {
        // Enforce unique employee number during creation.
        if (repository.existsById(request.employeeNo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Employee No already exists");
        }
        validateDuplicateSensitiveFields(request.employeeNo(), request);
        TechnicianRecord record = toRecord(request);
        Instant now = Instant.now();
        record.setCreatedAt(now);
        record.setUpdatedAt(now);
        record.setCreatedBy(currentActor());
        record.setUpdatedBy(currentActor());
        record.setArchived(Boolean.FALSE);
        TechnicianRecord saved = repository.save(record);
        auditLogService.record(saved.getEmployeeNo(), "CREATE", "Created employee profile");
        return saved;
    }

    public TechnicianRecord update(String employeeId, TechnicianRequest request) {
        // Load existing profile, then overwrite fields with request payload.
        TechnicianRecord existing = getByEmployeeId(employeeId);
        validateDuplicateSensitiveFields(employeeId, request);
        existing.setTitle(request.title());
        existing.setFullName(request.fullName());
        existing.setFirstName(request.firstName());
        existing.setMiddleName(request.middleName());
        existing.setLastName(request.lastName());
        existing.setGender(request.gender());
        existing.setDateOfBirth(request.dateOfBirth());
        existing.setAge(request.age());
        existing.setMaritalStatus(request.maritalStatus());
        existing.setJobTitle(request.jobTitle());
        existing.setCategory(request.category());
        existing.setPayrollStatus(request.payrollStatus());
        existing.setAmount(request.amount());
        existing.setGrading(request.grading());
        existing.setJobGroup(request.jobGroup());
        existing.setLocation(request.location());
        existing.setEmploymentDate(request.employmentDate());
        existing.setEntryChecklist(request.entryChecklist());
        existing.setStatus(request.status());
        existing.setExitDate(request.exitDate());
        existing.setLengthService(request.lengthService());
        existing.setExitChecklist(request.exitChecklist());
        existing.setAnnualLeaveEntitlement(request.annualLeaveEntitlement());
        existing.setLeaveTaken(request.leaveTaken());
        existing.setLeaveBalance(request.leaveBalance());
        existing.setSocialSecurityNo(request.socialSecurityNo());
        existing.setBankName(request.bankName());
        existing.setBankBranchName(request.bankBranchName());
        existing.setAccountNo(request.accountNo());
        existing.setTaxIdentificationNo(request.taxIdentificationNo());
        existing.setGhanaCardNumber(request.ghanaCardNumber());
        existing.setProvidentFund(request.providentFund());
        existing.setVehicleAndFuel(request.vehicleAndFuel());
        existing.setTransportAllowanceGallons(request.transportAllowanceGallons());
        existing.setCellPhone(request.cellPhone());
        existing.setAirtimeAmount(request.airtimeAmount());
        existing.setLunch(request.lunch());
        existing.setMedicalPlan(request.medicalPlan());
        existing.setSpouseOrNa(request.spouseOrNa());
        existing.setChildrenCount(request.childrenCount());
        existing.setMobilePhoneNo(request.mobilePhoneNo());
        existing.setBusinessPhoneNo(request.businessPhoneNo());
        existing.setHomePhoneNo(request.homePhoneNo());
        existing.setEmailAddress(request.emailAddress());
        existing.setPostalAddress(request.postalAddress());
        existing.setResidentialAddress(request.residentialAddress());
        existing.setEmergencyContact(request.emergencyContact());
        existing.setEmergencyPhoneNo(request.emergencyPhoneNo());
        existing.setRelationship(request.relationship());
        existing.setSpouseName(request.spouseName());
        existing.setNamesOfChildren(request.namesOfChildren());
        existing.setNumberOfDependents(request.numberOfDependents());
        existing.setNextOfKinName(request.nextOfKinName());
        existing.setNextOfKinContactDetails(request.nextOfKinContactDetails());
        existing.setGuarantorName(request.guarantorName());
        existing.setGuarantorContact(request.guarantorContact());
        existing.setGuarantorResidentialAddress(request.guarantorResidentialAddress());
        existing.setGuarantorGhanaCard(request.guarantorGhanaCard());
        existing.setCustomFieldsJson(toJson(request.customFields()));
        existing.setLegacyEmployeeName(request.fullName());
        existing.setLegacyTeam(request.jobGroup() == null ? "General" : request.jobGroup());
        existing.setLegacyTask(request.jobTitle() == null ? "Profile Update" : request.jobTitle());
        existing.setLegacyHoursLogged(0);
        existing.setLegacyLastUpdated(Instant.now().toString());
        existing.setUpdatedAt(Instant.now());
        existing.setUpdatedBy(currentActor());
        // Persist and return the latest saved state.
        TechnicianRecord saved = repository.save(existing);
        auditLogService.record(saved.getEmployeeNo(), "UPDATE", "Updated employee profile");
        return saved;
    }

    public void delete(String employeeId) {
        TechnicianRecord existing = getByEmployeeId(employeeId);
        existing.setArchived(Boolean.TRUE);
        existing.setArchivedAt(Instant.now());
        existing.setArchivedBy(currentActor());
        existing.setUpdatedAt(Instant.now());
        existing.setUpdatedBy(currentActor());
        repository.save(existing);
        auditLogService.record(existing.getEmployeeNo(), "ARCHIVE", "Archived employee profile");
    }

    public TechnicianRecord restore(String employeeId) {
        TechnicianRecord existing = repository.findById(employeeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee record not found"));
        existing.setArchived(Boolean.FALSE);
        existing.setArchivedAt(null);
        existing.setArchivedBy(null);
        existing.setUpdatedAt(Instant.now());
        existing.setUpdatedBy(currentActor());
        TechnicianRecord saved = repository.save(existing);
        auditLogService.record(saved.getEmployeeNo(), "RESTORE", "Restored archived employee profile");
        return saved;
    }

    public void recordExport(String details) {
        auditLogService.record("ALL", "EXPORT", details);
    }

    private TechnicianRecord toRecord(TechnicianRequest request) {
        // Map API request object into database entity for inserts.
        TechnicianRecord record = new TechnicianRecord();
        record.setEmployeeNo(request.employeeNo());
        record.setTitle(request.title());
        record.setFullName(request.fullName());
        record.setFirstName(request.firstName());
        record.setMiddleName(request.middleName());
        record.setLastName(request.lastName());
        record.setGender(request.gender());
        record.setDateOfBirth(request.dateOfBirth());
        record.setAge(request.age());
        record.setMaritalStatus(request.maritalStatus());
        record.setJobTitle(request.jobTitle());
        record.setCategory(request.category());
        record.setPayrollStatus(request.payrollStatus());
        record.setAmount(request.amount());
        record.setGrading(request.grading());
        record.setJobGroup(request.jobGroup());
        record.setLocation(request.location());
        record.setEmploymentDate(request.employmentDate());
        record.setEntryChecklist(request.entryChecklist());
        record.setStatus(request.status());
        record.setExitDate(request.exitDate());
        record.setLengthService(request.lengthService());
        record.setExitChecklist(request.exitChecklist());
        record.setAnnualLeaveEntitlement(request.annualLeaveEntitlement());
        record.setLeaveTaken(request.leaveTaken());
        record.setLeaveBalance(request.leaveBalance());
        record.setSocialSecurityNo(request.socialSecurityNo());
        record.setBankName(request.bankName());
        record.setBankBranchName(request.bankBranchName());
        record.setAccountNo(request.accountNo());
        record.setTaxIdentificationNo(request.taxIdentificationNo());
        record.setGhanaCardNumber(request.ghanaCardNumber());
        record.setProvidentFund(request.providentFund());
        record.setVehicleAndFuel(request.vehicleAndFuel());
        record.setTransportAllowanceGallons(request.transportAllowanceGallons());
        record.setCellPhone(request.cellPhone());
        record.setAirtimeAmount(request.airtimeAmount());
        record.setLunch(request.lunch());
        record.setMedicalPlan(request.medicalPlan());
        record.setSpouseOrNa(request.spouseOrNa());
        record.setChildrenCount(request.childrenCount());
        record.setMobilePhoneNo(request.mobilePhoneNo());
        record.setBusinessPhoneNo(request.businessPhoneNo());
        record.setHomePhoneNo(request.homePhoneNo());
        record.setEmailAddress(request.emailAddress());
        record.setPostalAddress(request.postalAddress());
        record.setResidentialAddress(request.residentialAddress());
        record.setEmergencyContact(request.emergencyContact());
        record.setEmergencyPhoneNo(request.emergencyPhoneNo());
        record.setRelationship(request.relationship());
        record.setSpouseName(request.spouseName());
        record.setNamesOfChildren(request.namesOfChildren());
        record.setNumberOfDependents(request.numberOfDependents());
        record.setNextOfKinName(request.nextOfKinName());
        record.setNextOfKinContactDetails(request.nextOfKinContactDetails());
        record.setGuarantorName(request.guarantorName());
        record.setGuarantorContact(request.guarantorContact());
        record.setGuarantorResidentialAddress(request.guarantorResidentialAddress());
        record.setGuarantorGhanaCard(request.guarantorGhanaCard());
        record.setCustomFieldsJson(toJson(request.customFields()));
        record.setLegacyEmployeeName(request.fullName());
        record.setLegacyTeam(request.jobGroup() == null ? "General" : request.jobGroup());
        record.setLegacyTask(request.jobTitle() == null ? "New Employee Profile" : request.jobTitle());
        record.setLegacyHoursLogged(0);
        record.setLegacyLastUpdated(Instant.now().toString());
        return record;
    }

    private String currentActor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            return "system";
        }
        return authentication.getName();
    }

    private void validateDuplicateSensitiveFields(String employeeNo, TechnicianRequest request) {
        List<TechnicianRecord> activeRecords = repository.findByArchivedFalse();
        String ghanaCardNumber = normalized(request.ghanaCardNumber());
        String socialSecurityNo = normalized(request.socialSecurityNo());
        String emailAddress = normalized(request.emailAddress());

        for (TechnicianRecord record : activeRecords) {
            if (record.getEmployeeNo().equals(employeeNo)) {
                continue;
            }
            if (!ghanaCardNumber.isBlank() && ghanaCardNumber.equals(normalized(record.getGhanaCardNumber()))) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Ghana Card Number already exists");
            }
            if (!socialSecurityNo.isBlank() && socialSecurityNo.equals(normalized(record.getSocialSecurityNo()))) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Social Security No already exists");
            }
            if (!emailAddress.isBlank() && emailAddress.equals(normalized(record.getEmailAddress()))) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email address already exists");
            }
        }
    }

    private String normalized(String value) {
        return value == null ? "" : value.trim().toLowerCase();
    }

    private String toJson(Map<String, String> customFields) {
        // Serialize custom field map to JSON string for DB storage.
        try {
            return objectMapper.writeValueAsString(customFields == null ? Map.of() : customFields);
        } catch (JsonProcessingException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid custom fields payload");
        }
    }
}
