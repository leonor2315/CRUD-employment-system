package com.hvac.workflow.config;

import com.hvac.workflow.model.TechnicianRecord;
import com.hvac.workflow.repository.TechnicianRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedData(TechnicianRepository repository) {
        return args -> {
            // Seed sample data only when the table is empty.
            if (repository.count() > 0) {
                return;
            }
            repository.saveAll(List.of(
                    build("EMP-001", "Ms.", "Ava Johnson", "Manager", "Finance", "Head Office", "Engaged"),
                    build("EMP-002", "Mr.", "Noah Smith", "Officer", "Sales", "Tema", "Engaged"),
                    build("EMP-003", "Mrs.", "Emma Brown", "Senior Officer", "HR & Admin", "Takoradi", "Resigned"),
                    build("EMP-004", "Mr.", "Liam Wilson", "Supervisory", "Operations", "Kumasi", "Engaged")
            ));
        };
    }

    private TechnicianRecord build(
            String employeeNo,
            String title,
            String fullName,
            String grading,
            String jobGroup,
            String location,
            String status
    ) {
        // Build a minimal starter profile used by the seed routine.
        TechnicianRecord record = new TechnicianRecord();
        record.setEmployeeNo(employeeNo);
        record.setTitle(title);
        record.setFullName(fullName);
        String[] names = fullName.split(" ");
        record.setFirstName(names.length > 0 ? names[0] : fullName);
        record.setLastName(names.length > 1 ? names[names.length - 1] : "");
        record.setGender("Female");
        record.setCategory("Permanent");
        record.setPayrollStatus("On payroll");
        record.setAmount(0.0);
        record.setGrading(grading);
        record.setJobGroup(jobGroup);
        record.setLocation(location);
        record.setEmploymentDate("2024-01-01");
        record.setEntryChecklist("Completed");
        record.setStatus(status);
        record.setProvidentFund(Boolean.TRUE);
        record.setCellPhone(Boolean.TRUE);
        record.setLunch(Boolean.TRUE);
        record.setMedicalPlan("Standard");
        return record;
    }
}
