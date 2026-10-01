package com.tadda.tadda_backend.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.tadda.tadda_backend.config.RazorpayConfig;
import com.tadda.tadda_backend.entity.RegistrationPayment;
import com.tadda.tadda_backend.entity.User;
import com.tadda.tadda_backend.repository.RegistrationPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RazorpayPaymentService {

    private final RazorpayConfig razorpayConfig;
    private final RegistrationPaymentRepository registrationPaymentRepository;

    public Order createOrder(Double amount, User user) throws Exception {

        RazorpayClient razorpayClient = new RazorpayClient(
                razorpayConfig.getKeyId(),
                razorpayConfig.getKeySecret()
        );

        JSONObject orderRequest = new JSONObject();

        orderRequest.put("amount", (int) (amount * 100));
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "TADDA_" + System.currentTimeMillis());

        Order order = razorpayClient.orders.create(orderRequest);

        RegistrationPayment payment = registrationPaymentRepository
                .findByUser(user)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Registration payment not found"
                        )
                );

        payment.setRazorpayOrderId(order.get("id"));

        registrationPaymentRepository.save(payment);

        return order;
    }

    public boolean verifyPaymentSignature(
            String razorpayOrderId,
            String razorpayPaymentId,
            String razorpaySignature
    ) throws Exception {

        String payload = razorpayOrderId + "|" + razorpayPaymentId;

        com.razorpay.Utils.verifySignature(
                payload,
                razorpaySignature,
                razorpayConfig.getKeySecret()
        );

        return true;
    }
}