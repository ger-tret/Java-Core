package com.payment.service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class PaymentEvent {

    private Long orderId;
    private Long paymentId;
    private String status;
}
