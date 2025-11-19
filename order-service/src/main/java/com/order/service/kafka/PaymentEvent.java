package com.order.service.kafka;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class PaymentEvent {

    private Long orderId;
    private String paymentId;
    private String status;
}