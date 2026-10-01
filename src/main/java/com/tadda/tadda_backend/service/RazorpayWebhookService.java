package com.tadda.tadda_backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import com.tadda.tadda_backend.config.RazorpayConfig;
import com.tadda.tadda_backend.entity.PaymentStatus;
import com.tadda.tadda_backend.entity.RegistrationPayment;
import com.tadda.tadda_backend.repository.RegistrationPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RazorpayWebhookService {

    private final RazorpayConfig razorpayConfig;
    private final RegistrationPaymentRepository registrationPaymentRepository;
    private final ObjectMapper objectMapper;

    public boolean verifySignature(
            String payload,
            String signature
    ) {

        try {
            return Utils.verifyWebhookSignature(
                    payload,
                    signature,
                    razorpayConfig.getWebhookSecret()
            );
        } catch (RazorpayException exception) {
            return false;
        }
    }

    public void processWebhook(String payload) {

        try {
            JsonNode root = objectMapper.readTree(payload);

            String event = root.path("event").asText();

            if ("payment.captured".equals(event)) {

                JsonNode paymentEntity = root
                        .path("payload")
                        .path("payment")
                        .path("entity");

                String razorpayPaymentId =
                        paymentEntity.path("id").asText();

                String razorpayOrderId =
                        paymentEntity.path("order_id").asText();

                String status =
                        paymentEntity.path("status").asText();

                if (!"captured".equals(status)) {
                    return;
                }

                RegistrationPayment payment =
                        registrationPaymentRepository
                                .findByRazorpayOrderId(razorpayOrderId)
                                .orElse(null);

                if (payment == null) {
                    return;
                }

                if (payment.getStatus() == PaymentStatus.PAID) {
                    return;
                }

                payment.setStatus(PaymentStatus.PAID);
                payment.setTransactionId(razorpayPaymentId);
                payment.setPaymentMethod("RAZORPAY");
                payment.setPaidAt(LocalDateTime.now());

                registrationPaymentRepository.save(payment);
            }

        } catch (Exception exception) {
            throw new RuntimeException(
                    "Failed to process Razorpay webhook",
                    exception
            );
        }
    }
}