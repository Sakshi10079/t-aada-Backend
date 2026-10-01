package com.tadda.tadda_backend.controller;

import com.tadda.tadda_backend.service.RazorpayWebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhooks")
@RequiredArgsConstructor
public class RazorpayWebhookController {

    private final RazorpayWebhookService razorpayWebhookService;

    @PostMapping("/razorpay")
    public ResponseEntity<String> handleRazorpayWebhook(
            @RequestBody String payload,
            @RequestHeader("X-Razorpay-Signature") String signature
    ) {

        boolean verified = razorpayWebhookService.verifySignature(
                payload,
                signature
        );

        if (!verified) {
            return ResponseEntity.badRequest()
                    .body("Invalid webhook signature");
        }

        razorpayWebhookService.processWebhook(payload);

        return ResponseEntity.ok("Webhook processed");
    }
}