package com.hvac.workflow.controller;

import com.hvac.workflow.model.PasswordResetRequest;
import com.hvac.workflow.model.UserAccount;
import com.hvac.workflow.model.UserAccountRequest;
import com.hvac.workflow.service.UserAccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class UserAccountController {

    private final UserAccountService userAccountService;

    public UserAccountController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @GetMapping
    public List<UserAccount> allUsers() {
        return userAccountService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserAccount createUser(@Valid @RequestBody UserAccountRequest request) {
        return userAccountService.create(request);
    }

    @PutMapping("/{id}")
    public UserAccount updateUser(@PathVariable Long id, @Valid @RequestBody UserAccountRequest request) {
        return userAccountService.update(id, request);
    }

    @PostMapping("/{id}/reset-password")
    public UserAccount resetPassword(@PathVariable Long id, @Valid @RequestBody PasswordResetRequest request) {
        return userAccountService.resetPassword(id, request);
    }
}
