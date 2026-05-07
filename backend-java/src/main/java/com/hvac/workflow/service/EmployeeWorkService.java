package com.hvac.workflow.service;

import com.hvac.workflow.model.TechnicianRecord;
import com.hvac.workflow.model.TechnicianRequest;
import com.hvac.workflow.repository.TechnicianRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeWorkService {

    private final TechnicianRepository repository;
    private final ObjectMapper objectMapper;

    public EmployeeWorkService(TechnicianRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    public List<TechnicianRecord> getAll() {
        // Return all employee records (admin dashboard list).
        return repository.findAll();
    }

    public TechnicianRecord getByEmployeeId(String employeeId) {
        // Fetch one record or return 404 when employee number does not exist.
        return repository.findById(employeeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee record not found"));
    }

    public TechnicianRecord create(TechnicianRequest request) {
        // Enforce unique employee number during creation.
        if (repository.existsById(request.employeeNo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Employee No already exists");
        }
        TechnicianRecord record = toRecord(request);
        return repository.save(record);
    }

    public TechnicianRecord update(String employeeId, TechnicianRequest request) {
        // Load existing profile, then overwrite fields with request payload.
        TechnicianRecord existing = getByEmployeeId(employeeId);
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
        // Persist and return the latest saved state.
        return repository.save(existing);
    }

    public void delete(String employeeId) {
        // Protect delete route with a not-found check for clearer API errors.
        if (!repository.existsById(employeeId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee record not found");
        }
        repository.deleteById(employeeId);
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

    private String toJson(Map<String, String> customFields) {
        // Serialize custom field map to JSON string for DB storage.
        try {
            return objectMapper.writeValueAsString(customFields == null ? Map.of() : customFields);
        } catch (JsonProcessingException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid custom fields payload");
        }
    }
}
