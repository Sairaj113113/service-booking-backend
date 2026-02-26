package com.example.servicebooking.controller;

import com.example.servicebooking.model.Booking;
import com.example.servicebooking.model.BookingStatus;
import com.example.servicebooking.model.Role;
import com.example.servicebooking.model.User;
import com.example.servicebooking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookingController {

    private final BookingRepository bookingRepository;

    @GetMapping("/bookings")
    public ResponseEntity<List<Booking>> getMyBookings(@AuthenticationPrincipal User user) {
        if (user.getRole() == Role.ADMIN) {
            return ResponseEntity.ok(bookingRepository.findAll());
        }
        return ResponseEntity.ok(bookingRepository.findByUserId(user.getId()));
    }

    @GetMapping("/provider/bookings")
    @PreAuthorize("hasAnyRole('PROVIDER', 'ADMIN')")
    public ResponseEntity<List<Booking>> getProviderBookings(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(bookingRepository.findByService_ProviderId(user.getId()));
    }

    @PostMapping("/bookings")
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking, @AuthenticationPrincipal User user) {
        booking.setUserId(user.getId());
        booking.setStatus(BookingStatus.PENDING);
        return ResponseEntity.ok(bookingRepository.save(booking));
    }

    @PutMapping("/provider/bookings/{id}/status")
    @PreAuthorize("hasAnyRole('PROVIDER', 'ADMIN')")
    public ResponseEntity<Booking> updateBookingStatus(@PathVariable UUID id, @RequestBody Booking statusUpdate) {
        Booking booking = bookingRepository.findById(id).orElseThrow();
        booking.setStatus(statusUpdate.getStatus());
        return ResponseEntity.ok(bookingRepository.save(booking));
    }
}
