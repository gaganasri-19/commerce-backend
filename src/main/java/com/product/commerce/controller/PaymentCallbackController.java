package com.product.commerce.controller;

import com.product.commerce.service.OrderPaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/internal/payments")
public class PaymentCallbackController {

    private final OrderPaymentService orderPaymentService;

    public PaymentCallbackController(OrderPaymentService orderPaymentService) {
        this.orderPaymentService = orderPaymentService;
    }

    @PostMapping("/success/{orderId}")
    public ResponseEntity<Void> paymentSuccess(@PathVariable Long orderId) {
        orderPaymentService.markPaid(orderId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/failed/{orderId}")
    public ResponseEntity<Void> paymentFailed(@PathVariable Long orderId) {
        orderPaymentService.markFailed(orderId);
        return ResponseEntity.ok().build();
    }
}

