package com.tadda.tadda_backend.controller;

import com.tadda.tadda_backend.dto.AdminTrainingBookingResponseDto;
import com.tadda.tadda_backend.service.TrainingBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/training-bookings")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminTrainingBookingController {

    private final TrainingBookingService trainingBookingService;

    @GetMapping
    public ResponseEntity<List<AdminTrainingBookingResponseDto>> getAllTrainingBookings() {

        return ResponseEntity.ok(
                trainingBookingService.getAllBookingsForAdmin()
        );
    }
}