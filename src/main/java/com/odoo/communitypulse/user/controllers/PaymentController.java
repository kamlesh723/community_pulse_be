package com.odoo.communitypulse.user.controllers;

import com.odoo.communitypulse.user.service.StripePaymentService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class PaymentController {

    @Autowired
    private StripePaymentService stripePaymentService;

    @PostMapping("/create-payment-intent")
    public ResponseEntity<Map<String, String>> createPaymentIntent(@RequestBody Map<String, Object> data) {
        Map<String, String> response = new HashMap<>();

        try {
            // Extract amount from request body
            Object amountObj = data.get("amount");
            if (amountObj == null) {
                response.put("error", "Amount is required");
                return ResponseEntity.badRequest().body(response);
            }

            // Convert amount to double
            double amount;
            if (amountObj instanceof Number) {
                amount = ((Number) amountObj).doubleValue();
            } else {
                response.put("error", "Amount must be a number");
                return ResponseEntity.badRequest().body(response);
            }

            // Call service to create payment intent
            String clientSecret = stripePaymentService.createPaymentIntent(amount, "inr");
            response.put("clientSecret", clientSecret);
            return ResponseEntity.ok(response);

        } catch (StripeException e) {
            e.printStackTrace();
            response.put("error", "Failed to create payment intent: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
