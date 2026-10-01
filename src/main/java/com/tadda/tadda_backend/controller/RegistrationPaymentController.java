package com.tadda.tadda_backend.controller;

import com.razorpay.Order;
import com.tadda.tadda_backend.config.RazorpayConfig;
import com.tadda.tadda_backend.dto.RazorpayPaymentVerificationRequestDto;
import com.tadda.tadda_backend.entity.RegistrationPayment;
import com.tadda.tadda_backend.entity.User;
import com.tadda.tadda_backend.service.RazorpayPaymentService;
import com.tadda.tadda_backend.service.RegistrationPaymentService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/registration-payment")
@RequiredArgsConstructor
@PreAuthorize("hasRole('BRAND_OWNER')")
public class RegistrationPaymentController {

    private final RazorpayPaymentService razorpayPaymentService;
    private final RazorpayConfig razorpayConfig;
    private final RegistrationPaymentService registrationPaymentService;

    @PostMapping("/order")
    public ResponseEntity<?> createRegistrationPaymentOrder(
            Authentication authentication
    ) {
        try {
            User user = (User) authentication.getPrincipal();

            Order order = razorpayPaymentService.createOrder(
                    1799.0,
                    user
            );

            JSONObject orderJson = new JSONObject(order.toString());

            return ResponseEntity.ok(
                    Map.of(
                            "orderId", orderJson.getString("id"),
                            "amount", orderJson.getInt("amount"),
                            "currency", orderJson.getString("currency"),
                            "keyId", razorpayConfig.getKeyId()
                    )
            );

        } catch (Exception exception) {
            return ResponseEntity
                    .internalServerError()
                    .body("Failed to create registration payment order");
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyRegistrationPayment(
            @RequestBody RazorpayPaymentVerificationRequestDto request, Authentication authentication
    ) {
        try {
            boolean verified = razorpayPaymentService.verifyPaymentSignature(
                    request.razorpayOrderId(),
                    request.razorpayPaymentId(),
                    request.razorpaySignature()
            );

            if (!verified) {
                return ResponseEntity.badRequest().body(
                        Map.of(
                                "verified", false,
                                "message", "Payment verification failed"
                        )
                );
            }

            User user = (User) authentication.getPrincipal();

            registrationPaymentService.markAsPaid(
                    request.razorpayOrderId(),
                    request.razorpayPaymentId(),
                    user
            );

            return ResponseEntity.ok(
                    Map.of(
                            "verified", true,
                            "message", "Payment verified and registration payment marked as PAID"
                    )
            );

        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "verified", false,
                            "message", "Payment verification failed"
                    )
            );
        }
    }

    @GetMapping("/status")
    public ResponseEntity<?> getRegistrationPaymentStatus(
            Authentication authentication
    ) {
        try {
            User user = (User) authentication.getPrincipal();

            RegistrationPayment payment =
                    registrationPaymentService.getByUser(user);

            return ResponseEntity.ok(
                    Map.of(
                            "status",
                            payment.getStatus().name()
                    )
            );

        } catch (Exception exception) {
            return ResponseEntity
                    .internalServerError()
                    .body(
                            Map.of(
                                    "message",
                                    exception.getMessage()
                            )
                    );
        }
    }
}