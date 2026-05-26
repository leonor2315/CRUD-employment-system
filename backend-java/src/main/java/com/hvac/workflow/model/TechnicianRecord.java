package com.hvac.workflow.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "technician")
// JPA entity mapped to the technician table used by the HVAC workflow.
public class TechnicianRecord {

    @Id
    @Column(name = "employee_no", nullable = false, updatable = false)
    private String employeeNo;
    private String title;
    private String fullName;
    private String firstName;
    private String middleName;
    private String lastName;
    private String gender;
    private String dateOfBirth;
    private Integer age;
    private String maritalStatus;
    private String jobTitle;
    private String category;
    private String payrollStatus;
    private Double amount;
    private String grading;
    private String jobGroup;
    private String location;
    private String employmentDate;
    private String entryChecklist;
    private String status;
    private String exitDate;
    private String lengthService;
    @Column(columnDefinition = "TEXT")
    private String exitChecklist;
    private Integer annualLeaveEntitlement;
    private Integer leaveTaken;
    private Integer leaveBalance;
    private String socialSecurityNo;
    private String bankName;
    private String bankBranchName;
    private String accountNo;
    private String taxIdentificationNo;
    private String ghanaCardNumber;
    private Boolean providentFund;
    private String vehicleAndFuel;
    private Double transportAllowanceGallons;
    private Boolean cellPhone;
    private Double airtimeAmount;
    private Boolean lunch;
    private String medicalPlan;
    private String spouseOrNa;
    private Integer childrenCount;
    private String mobilePhoneNo;
    private String businessPhoneNo;
    private String homePhoneNo;
    private String emailAddress;
    private String postalAddress;
    @Column(columnDefinition = "TEXT")
    private String residentialAddress;
    private String emergencyContact;
    private String emergencyPhoneNo;
    private String relationship;
    private String spouseName;
    @Column(columnDefinition = "TEXT")
    private String namesOfChildren;
    private Integer numberOfDependents;
    private String nextOfKinName;
    @Column(columnDefinition = "TEXT")
    private String nextOfKinContactDetails;
    private String guarantorName;
    private String guarantorContact;
    @Column(columnDefinition = "TEXT")
    private String guarantorResidentialAddress;
    private String guarantorGhanaCard;
    @Column(columnDefinition = "TEXT")
    private String customFieldsJson;
    // Legacy fields kept for backward compatibility with earlier schema/views.
    @Column(name = "employee_name")
    private String legacyEmployeeName;
    @Column(name = "team")
    private String legacyTeam;
    @Column(name = "task")
    private String legacyTask;
    @Column(name = "hours_logged")
    private Integer legacyHoursLogged;
    @Column(name = "last_updated")
    private String legacyLastUpdated;
    private Boolean archived = Boolean.FALSE;
    private Instant archivedAt;
    private String archivedBy;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;

    public String getEmployeeNo() { return employeeNo; }
    public void setEmployeeNo(String employeeNo) { this.employeeNo = employeeNo; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public String getMaritalStatus() { return maritalStatus; }
    public void setMaritalStatus(String maritalStatus) { this.maritalStatus = maritalStatus; }
    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getPayrollStatus() { return payrollStatus; }
    public void setPayrollStatus(String payrollStatus) { this.payrollStatus = payrollStatus; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public String getGrading() { return grading; }
    public void setGrading(String grading) { this.grading = grading; }
    public String getJobGroup() { return jobGroup; }
    public void setJobGroup(String jobGroup) { this.jobGroup = jobGroup; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getEmploymentDate() { return employmentDate; }
    public void setEmploymentDate(String employmentDate) { this.employmentDate = employmentDate; }
    public String getEntryChecklist() { return entryChecklist; }
    public void setEntryChecklist(String entryChecklist) { this.entryChecklist = entryChecklist; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getExitDate() { return exitDate; }
    public void setExitDate(String exitDate) { this.exitDate = exitDate; }
    public String getLengthService() { return lengthService; }
    public void setLengthService(String lengthService) { this.lengthService = lengthService; }
    public String getExitChecklist() { return exitChecklist; }
    public void setExitChecklist(String exitChecklist) { this.exitChecklist = exitChecklist; }
    public Integer getAnnualLeaveEntitlement() { return annualLeaveEntitlement; }
    public void setAnnualLeaveEntitlement(Integer annualLeaveEntitlement) { this.annualLeaveEntitlement = annualLeaveEntitlement; }
    public Integer getLeaveTaken() { return leaveTaken; }
    public void setLeaveTaken(Integer leaveTaken) { this.leaveTaken = leaveTaken; }
    public Integer getLeaveBalance() { return leaveBalance; }
    public void setLeaveBalance(Integer leaveBalance) { this.leaveBalance = leaveBalance; }
    public String getSocialSecurityNo() { return socialSecurityNo; }
    public void setSocialSecurityNo(String socialSecurityNo) { this.socialSecurityNo = socialSecurityNo; }
    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }
    public String getBankBranchName() { return bankBranchName; }
    public void setBankBranchName(String bankBranchName) { this.bankBranchName = bankBranchName; }
    public String getAccountNo() { return accountNo; }
    public void setAccountNo(String accountNo) { this.accountNo = accountNo; }
    public String getTaxIdentificationNo() { return taxIdentificationNo; }
    public void setTaxIdentificationNo(String taxIdentificationNo) { this.taxIdentificationNo = taxIdentificationNo; }
    public String getGhanaCardNumber() { return ghanaCardNumber; }
    public void setGhanaCardNumber(String ghanaCardNumber) { this.ghanaCardNumber = ghanaCardNumber; }
    public Boolean getProvidentFund() { return providentFund; }
    public void setProvidentFund(Boolean providentFund) { this.providentFund = providentFund; }
    public String getVehicleAndFuel() { return vehicleAndFuel; }
    public void setVehicleAndFuel(String vehicleAndFuel) { this.vehicleAndFuel = vehicleAndFuel; }
    public Double getTransportAllowanceGallons() { return transportAllowanceGallons; }
    public void setTransportAllowanceGallons(Double transportAllowanceGallons) { this.transportAllowanceGallons = transportAllowanceGallons; }
    public Boolean getCellPhone() { return cellPhone; }
    public void setCellPhone(Boolean cellPhone) { this.cellPhone = cellPhone; }
    public Double getAirtimeAmount() { return airtimeAmount; }
    public void setAirtimeAmount(Double airtimeAmount) { this.airtimeAmount = airtimeAmount; }
    public Boolean getLunch() { return lunch; }
    public void setLunch(Boolean lunch) { this.lunch = lunch; }
    public String getMedicalPlan() { return medicalPlan; }
    public void setMedicalPlan(String medicalPlan) { this.medicalPlan = medicalPlan; }
    public String getSpouseOrNa() { return spouseOrNa; }
    public void setSpouseOrNa(String spouseOrNa) { this.spouseOrNa = spouseOrNa; }
    public Integer getChildrenCount() { return childrenCount; }
    public void setChildrenCount(Integer childrenCount) { this.childrenCount = childrenCount; }
    public String getMobilePhoneNo() { return mobilePhoneNo; }
    public void setMobilePhoneNo(String mobilePhoneNo) { this.mobilePhoneNo = mobilePhoneNo; }
    public String getBusinessPhoneNo() { return businessPhoneNo; }
    public void setBusinessPhoneNo(String businessPhoneNo) { this.businessPhoneNo = businessPhoneNo; }
    public String getHomePhoneNo() { return homePhoneNo; }
    public void setHomePhoneNo(String homePhoneNo) { this.homePhoneNo = homePhoneNo; }
    public String getEmailAddress() { return emailAddress; }
    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }
    public String getPostalAddress() { return postalAddress; }
    public void setPostalAddress(String postalAddress) { this.postalAddress = postalAddress; }
    public String getResidentialAddress() { return residentialAddress; }
    public void setResidentialAddress(String residentialAddress) { this.residentialAddress = residentialAddress; }
    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }
    public String getEmergencyPhoneNo() { return emergencyPhoneNo; }
    public void setEmergencyPhoneNo(String emergencyPhoneNo) { this.emergencyPhoneNo = emergencyPhoneNo; }
    public String getRelationship() { return relationship; }
    public void setRelationship(String relationship) { this.relationship = relationship; }
    public String getSpouseName() { return spouseName; }
    public void setSpouseName(String spouseName) { this.spouseName = spouseName; }
    public String getNamesOfChildren() { return namesOfChildren; }
    public void setNamesOfChildren(String namesOfChildren) { this.namesOfChildren = namesOfChildren; }
    public Integer getNumberOfDependents() { return numberOfDependents; }
    public void setNumberOfDependents(Integer numberOfDependents) { this.numberOfDependents = numberOfDependents; }
    public String getNextOfKinName() { return nextOfKinName; }
    public void setNextOfKinName(String nextOfKinName) { this.nextOfKinName = nextOfKinName; }
    public String getNextOfKinContactDetails() { return nextOfKinContactDetails; }
    public void setNextOfKinContactDetails(String nextOfKinContactDetails) { this.nextOfKinContactDetails = nextOfKinContactDetails; }
    public String getGuarantorName() { return guarantorName; }
    public void setGuarantorName(String guarantorName) { this.guarantorName = guarantorName; }
    public String getGuarantorContact() { return guarantorContact; }
    public void setGuarantorContact(String guarantorContact) { this.guarantorContact = guarantorContact; }
    public String getGuarantorResidentialAddress() { return guarantorResidentialAddress; }
    public void setGuarantorResidentialAddress(String guarantorResidentialAddress) { this.guarantorResidentialAddress = guarantorResidentialAddress; }
    public String getGuarantorGhanaCard() { return guarantorGhanaCard; }
    public void setGuarantorGhanaCard(String guarantorGhanaCard) { this.guarantorGhanaCard = guarantorGhanaCard; }
    public String getCustomFieldsJson() { return customFieldsJson; }
    public void setCustomFieldsJson(String customFieldsJson) { this.customFieldsJson = customFieldsJson; }
    public String getLegacyEmployeeName() { return legacyEmployeeName; }
    public void setLegacyEmployeeName(String legacyEmployeeName) { this.legacyEmployeeName = legacyEmployeeName; }
    public String getLegacyTeam() { return legacyTeam; }
    public void setLegacyTeam(String legacyTeam) { this.legacyTeam = legacyTeam; }
    public String getLegacyTask() { return legacyTask; }
    public void setLegacyTask(String legacyTask) { this.legacyTask = legacyTask; }
    public Integer getLegacyHoursLogged() { return legacyHoursLogged; }
    public void setLegacyHoursLogged(Integer legacyHoursLogged) { this.legacyHoursLogged = legacyHoursLogged; }
    public String getLegacyLastUpdated() { return legacyLastUpdated; }
    public void setLegacyLastUpdated(String legacyLastUpdated) { this.legacyLastUpdated = legacyLastUpdated; }
    public Boolean getArchived() { return archived; }
    public void setArchived(Boolean archived) { this.archived = archived; }
    public Instant getArchivedAt() { return archivedAt; }
    public void setArchivedAt(Instant archivedAt) { this.archivedAt = archivedAt; }
    public String getArchivedBy() { return archivedBy; }
    public void setArchivedBy(String archivedBy) { this.archivedBy = archivedBy; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
}
