package com.hvac.workflow.controller;

import com.hvac.workflow.service.DatabaseUserDetailsService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final DatabaseUserDetailsService userDetailsService;

    public AuthController(DatabaseUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/me")
    public Map<String, Object> currentUser(Authentication authentication) {
        userDetailsService.markLogin(authentication.getName());
        List<String> roles = authentication.getAuthorities().stream()
                .map(authority -> authority.getAuthority().replaceFirst("^ROLE_", ""))
                .toList();

        return Map.of(
                "username", authentication.getName(),
                "roles", roles
        );
    }
}
