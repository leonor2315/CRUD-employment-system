package com.hvac.workflow.service;

import com.hvac.workflow.model.PasswordResetRequest;
import com.hvac.workflow.model.UserAccount;
import com.hvac.workflow.model.UserAccountRequest;
import com.hvac.workflow.repository.UserAccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
public class UserAccountService {

    private final UserAccountRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserAccountService(UserAccountRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserAccount> getAll() {
        return repository.findAll();
    }

    public UserAccount create(UserAccountRequest request) {
        if (repository.existsByUsernameIgnoreCase(request.username())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }
        if (request.password() == null || request.password().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is required for new users");
        }

        Instant now = Instant.now();
        UserAccount account = new UserAccount();
        account.setUsername(request.username().trim().toLowerCase());
        account.setDisplayName(request.displayName().trim());
        account.setRole(request.role());
        account.setPasswordHash(passwordEncoder.encode(request.password()));
        account.setEnabled(request.enabled() == null || request.enabled());
        account.setCreatedAt(now);
        account.setUpdatedAt(now);
        return repository.save(account);
    }

    public UserAccount update(Long id, UserAccountRequest request) {
        UserAccount account = getById(id);
        account.setDisplayName(request.displayName().trim());
        account.setRole(request.role());
        account.setEnabled(request.enabled() == null || request.enabled());
        account.setUpdatedAt(Instant.now());
        return repository.save(account);
    }

    public UserAccount resetPassword(Long id, PasswordResetRequest request) {
        UserAccount account = getById(id);
        account.setPasswordHash(passwordEncoder.encode(request.password()));
        account.setUpdatedAt(Instant.now());
        return repository.save(account);
    }

    private UserAccount getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User account not found"));
    }
}
