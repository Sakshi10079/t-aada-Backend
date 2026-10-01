package com.tadda.tadda_backend.controller;

import com.tadda.tadda_backend.dto.AdminRegistrationPaymentResponseDto;
import com.tadda.tadda_backend.service.RegistrationPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/registration-payments")
@RequiredArgsConstructor
public class AdminRegistrationPaymentController {

    private final RegistrationPaymentService registrationPaymentService;

    @GetMapping
    public ResponseEntity<List<AdminRegistrationPaymentResponseDto>> getAllPayments() {
        return ResponseEntity.ok(
                registrationPaymentService.getAllPaymentsForAdmin()
        );
    }
}