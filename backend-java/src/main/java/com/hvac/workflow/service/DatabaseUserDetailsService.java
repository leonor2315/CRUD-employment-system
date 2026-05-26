package com.hvac.workflow.service;

import com.hvac.workflow.model.UserAccount;
import com.hvac.workflow.repository.UserAccountRepository;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    private final UserAccountRepository repository;
    private final LoginAttemptService loginAttemptService;

    public DatabaseUserDetailsService(UserAccountRepository repository, LoginAttemptService loginAttemptService) {
        this.repository = repository;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (loginAttemptService.isBlocked(username)) {
            throw new LockedException("Account temporarily locked");
        }

        UserAccount account = repository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return User.withUsername(account.getUsername())
                .password(account.getPasswordHash())
                .roles(account.getRole())
                .disabled(!Boolean.TRUE.equals(account.getEnabled()))
                .build();
    }

    public void markLogin(String username) {
        repository.findByUsernameIgnoreCase(username).ifPresent(account -> {
            account.setLastLoginAt(Instant.now());
            repository.save(account);
        });
    }
}
