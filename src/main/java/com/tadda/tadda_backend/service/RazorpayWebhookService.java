package com.tadda.tadda_backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import com.tadda.tadda_backend.config.RazorpayConfig;
import com.tadda.tadda_backend.entity.PaymentStatus;
import com.tadda.tadda_backend.entity.RegistrationPayment;
import com.tadda.tadda_backend.entity.TrainingBooking;
import com.tadda.tadda_backend.entity.TrainingPaymentStatus;
import com.tadda.tadda_backend.repository.RegistrationPaymentRepository;
import com.tadda.tadda_backend.repository.TrainingBookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RazorpayWebhookService {

    private final RazorpayConfig razorpayConfig;
    private final RegistrationPaymentRepository registrationPaymentRepository;
    private final TrainingBookingRepository trainingBookingRepository;
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

                /*
                 * First check whether this is a Brand Owner
                 * registration payment.
                 */
                RegistrationPayment registrationPayment =
                        registrationPaymentRepository
                                .findByRazorpayOrderId(razorpayOrderId)
                                .orElse(null);

                if (registrationPayment != null) {

                    if (registrationPayment.getStatus()
                            == PaymentStatus.PAID) {
                        return;
                    }

                    registrationPayment.setStatus(
                            PaymentStatus.PAID
                    );

                    registrationPayment.setTransactionId(
                            razorpayPaymentId
                    );

                    registrationPayment.setPaymentMethod(
                            "RAZORPAY"
                    );

                    registrationPayment.setPaidAt(
                            LocalDateTime.now()
                    );

                    registrationPaymentRepository.save(
                            registrationPayment
                    );

                    return;
                }

                /*
                 * If it wasn't a registration payment,
                 * check whether it is a training booking.
                 */
                TrainingBooking trainingBooking =
                        trainingBookingRepository
                                .findByRazorpayOrderId(razorpayOrderId)
                                .orElse(null);

                if (trainingBooking != null) {

                    if (trainingBooking.getPaymentStatus()
                            == TrainingPaymentStatus.PAID) {
                        return;
                    }

                    trainingBooking.setPaymentStatus(
                            TrainingPaymentStatus.PAID
                    );

                    trainingBooking.setBookingStatus(
                            com.tadda.tadda_backend.entity.TrainingBookingStatus.CONFIRMED
                    );

                    trainingBooking.setTransactionId(
                            razorpayPaymentId
                    );

                    trainingBooking.setPaymentMethod(
                            "RAZORPAY"
                    );

                    trainingBookingRepository.save(
                            trainingBooking
                    );
                }
            }

        } catch (Exception exception) {

            throw new RuntimeException(
                    "Failed to process Razorpay webhook",
                    exception
            );
        }
    }
}