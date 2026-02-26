package com.example.servicebooking.controller;

import com.example.servicebooking.model.Role;
import com.example.servicebooking.model.User;
import com.example.servicebooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/providers/pending")
    public ResponseEntity<List<User>> getPendingProviders() {
        return ResponseEntity.ok(userRepository.findAll().stream()
                .filter(u -> u.getRole() == Role.PROVIDER && !u.isApproved())
                .toList());
    }

    @PutMapping("/providers/{id}/approve")
    public ResponseEntity<?> approveProvider(@PathVariable UUID id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setApproved(true);
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }
}
