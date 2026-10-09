package com.tadda.tadda_backend.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import com.tadda.tadda_backend.config.RazorpayConfig;
import com.tadda.tadda_backend.entity.TrainingBooking;
import com.tadda.tadda_backend.repository.TrainingBookingRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrainingRazorpayPaymentService {

    private static final double TRAINING_FEE = 99.0;

    private final RazorpayConfig razorpayConfig;
    private final TrainingBookingRepository trainingBookingRepository;

    public Order createOrder(TrainingBooking booking) throws Exception {

        RazorpayClient razorpayClient = new RazorpayClient(
                razorpayConfig.getKeyId(),
                razorpayConfig.getKeySecret()
        );

        JSONObject orderRequest = new JSONObject();

        orderRequest.put("amount", (int) (TRAINING_FEE * 100));
        orderRequest.put("currency", "INR");
        orderRequest.put(
                "receipt",
                "TADDA_TRAINING_" + System.currentTimeMillis()
        );

        Order order = razorpayClient.orders.create(orderRequest);

        booking.setRazorpayOrderId(order.get("id"));
        trainingBookingRepository.save(booking);

        return order;
    }

    public boolean verifyPaymentSignature(
            String razorpayOrderId,
            String razorpayPaymentId,
            String razorpaySignature
    ) throws Exception {

        String payload =
                razorpayOrderId + "|" + razorpayPaymentId;

        Utils.verifySignature(
                payload,
                razorpaySignature,
                razorpayConfig.getKeySecret()
        );

        return true;
    }
}