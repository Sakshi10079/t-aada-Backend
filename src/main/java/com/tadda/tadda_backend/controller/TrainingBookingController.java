package com.tadda.tadda_backend.controller;

import com.razorpay.Order;
import com.tadda.tadda_backend.config.RazorpayConfig;
import com.tadda.tadda_backend.dto.TrainingBookingRequestDto;
import com.tadda.tadda_backend.entity.TrainingBooking;
import com.tadda.tadda_backend.service.TrainingBookingService;
import com.tadda.tadda_backend.service.TrainingRazorpayPaymentService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/training/bookings")
@RequiredArgsConstructor
public class TrainingBookingController {

    private final TrainingBookingService trainingBookingService;
    private final TrainingRazorpayPaymentService trainingRazorpayPaymentService;
    private final RazorpayConfig razorpayConfig;

    @PostMapping("/order")
    public ResponseEntity<?> createTrainingBookingOrder(
            @RequestBody TrainingBookingRequestDto request
    ) {

        try {
            TrainingBooking booking =
                    trainingBookingService.createBooking(
                            request.name(),
                            request.email(),
                            request.phone(),
                            request.trainingDate()
                    );

            Order order =
                    trainingRazorpayPaymentService.createOrder(booking);

            JSONObject orderJson =
                    new JSONObject(order.toString());

            return ResponseEntity.ok(
                    Map.of(
                            "orderId", orderJson.getString("id"),
                            "amount", orderJson.getInt("amount"),
                            "currency", orderJson.getString("currency"),
                            "keyId", razorpayConfig.getKeyId()
                    )
            );

        } catch (Exception exception) {

            return ResponseEntity.badRequest()
                    .body(
                            Map.of(
                                    "message",
                                    exception.getMessage()
                            )
                    );
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyTrainingPayment(
            @RequestBody Map<String, String> request
    ) {

        try {
            String razorpayOrderId =
                    request.get("razorpayOrderId");

            String razorpayPaymentId =
                    request.get("razorpayPaymentId");

            String razorpaySignature =
                    request.get("razorpaySignature");

            boolean verified =
                    trainingRazorpayPaymentService.verifyPaymentSignature(
                            razorpayOrderId,
                            razorpayPaymentId,
                            razorpaySignature
                    );

            if (!verified) {
                return ResponseEntity.badRequest()
                        .body(
                                Map.of(
                                        "verified", false,
                                        "message",
                                        "Payment verification failed"
                                )
                        );
            }

            trainingBookingService.markAsPaid(
                    razorpayOrderId,
                    razorpayPaymentId
            );

            return ResponseEntity.ok(
                    Map.of(
                            "verified", true,
                            "message",
                            "Training booking confirmed successfully"
                    )
            );

        } catch (Exception exception) {

            return ResponseEntity.badRequest()
                    .body(
                            Map.of(
                                    "verified", false,
                                    "message",
                                    "Payment verification failed"
                            )
                    );
        }
    }
}