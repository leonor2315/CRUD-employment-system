package com.hvac.workflow.config;

import com.hvac.workflow.model.TechnicianRecord;
import com.hvac.workflow.model.UserAccount;
import com.hvac.workflow.repository.TechnicianRepository;
import com.hvac.workflow.repository.UserAccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedData(
            TechnicianRepository repository,
            UserAccountRepository userAccountRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            if (userAccountRepository.count() == 0) {
                Instant now = Instant.now();
                userAccountRepository.saveAll(List.of(
                        user("humanresource", "Human Resource", "ADMIN", "HRMI056", passwordEncoder, now),
                        user("MD", "Managing Director", "DIRECTOR", "MD056", passwordEncoder, now),
                        user("manager", "Manager", "MANAGER", "manager123", passwordEncoder, now),
                        user("employee", "Employee", "EMPLOYEE", "employee123", passwordEncoder, now)
                ));
            } else if (
                    userAccountRepository.findByUsernameIgnoreCase("MD").isEmpty()
                            && userAccountRepository.findByUsernameIgnoreCase("managingdirector").isPresent()
            ) {
                UserAccount directorAccount = userAccountRepository.findByUsernameIgnoreCase("managingdirector").orElseThrow();
                directorAccount.setUsername("MD");
                directorAccount.setUpdatedAt(Instant.now());
                userAccountRepository.save(directorAccount);
            }

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

    private UserAccount user(
            String username,
            String displayName,
            String role,
            String password,
            PasswordEncoder passwordEncoder,
            Instant now
    ) {
        UserAccount account = new UserAccount();
        account.setUsername(username);
        account.setDisplayName(displayName);
        account.setRole(role);
        account.setPasswordHash(passwordEncoder.encode(password));
        account.setEnabled(Boolean.TRUE);
        account.setCreatedAt(now);
        account.setUpdatedAt(now);
        return account;
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
