package com.hvac.workflow.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptService {

    private final Map<String, AttemptState> attempts = new ConcurrentHashMap<>();
    private final int maxFailedAttempts;
    private final Duration lockDuration;

    public LoginAttemptService(
            @Value("${app.security.login.max-failed-attempts:5}") int maxFailedAttempts,
            @Value("${app.security.login.lock-duration-minutes:15}") long lockDurationMinutes
    ) {
        this.maxFailedAttempts = Math.max(1, maxFailedAttempts);
        this.lockDuration = Duration.ofMinutes(Math.max(1, lockDurationMinutes));
    }

    public boolean isBlocked(String username) {
        String key = key(username);
        if (key.isBlank()) {
            return false;
        }

        AttemptState state = attempts.get(key);
        if (state == null || state.lockedUntil == null) {
            return false;
        }

        if (Instant.now().isBefore(state.lockedUntil)) {
            return true;
        }

        attempts.remove(key);
        return false;
    }

    public void recordFailure(String username) {
        String key = key(username);
        if (key.isBlank()) {
            return;
        }

        attempts.compute(key, (ignored, current) -> {
            Instant now = Instant.now();
            if (current != null && current.lockedUntil != null && now.isBefore(current.lockedUntil)) {
                return current;
            }

            int failures = current == null ? 1 : current.failures + 1;
            Instant lockedUntil = failures >= maxFailedAttempts ? now.plus(lockDuration) : null;
            return new AttemptState(failures, lockedUntil);
        });
    }

    public void recordSuccess(String username) {
        String key = key(username);
        if (!key.isBlank()) {
            attempts.remove(key);
        }
    }

    private String key(String username) {
        return String.valueOf(username).trim().toLowerCase(Locale.ROOT);
    }

    private record AttemptState(int failures, Instant lockedUntil) {
    }
}
