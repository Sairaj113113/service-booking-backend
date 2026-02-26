package com.example.servicebooking.controller;

import com.example.servicebooking.model.Role;
import com.example.servicebooking.model.Service;
import com.example.servicebooking.model.User;
import com.example.servicebooking.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceRepository serviceRepository;

    @GetMapping
    public ResponseEntity<List<Service>> getAllServices() {
        return ResponseEntity.ok(serviceRepository.findAll());
    }

    @GetMapping("/provider/{id}")
    @PreAuthorize("hasAnyRole('PROVIDER', 'ADMIN')")
    public ResponseEntity<List<Service>> getProviderServices(@PathVariable UUID id) {
        return ResponseEntity.ok(serviceRepository.findByProviderId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('PROVIDER', 'ADMIN')")
    public ResponseEntity<Service> createService(@RequestBody Service service, @AuthenticationPrincipal User user) {
        service.setProviderId(user.getId());
        return ResponseEntity.ok(serviceRepository.save(service));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('PROVIDER', 'ADMIN')")
    public ResponseEntity<Service> updateService(@PathVariable UUID id, @RequestBody Service serviceDetails) {
        Service service = serviceRepository.findById(id).orElseThrow();
        service.setName(serviceDetails.getName());
        service.setDescription(serviceDetails.getDescription());
        service.setPrice(serviceDetails.getPrice());
        service.setDuration(serviceDetails.getDuration());
        service.setCategory(serviceDetails.getCategory());
        service.setImageUrl(serviceDetails.getImageUrl());
        service.setIcon(serviceDetails.getIcon());
        return ResponseEntity.ok(serviceRepository.save(service));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('PROVIDER', 'ADMIN')")
    public ResponseEntity<?> deleteService(@PathVariable UUID id) {
        serviceRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
