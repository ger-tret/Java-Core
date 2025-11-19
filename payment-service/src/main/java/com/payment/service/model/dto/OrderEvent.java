package com.payment.service.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@RequiredArgsConstructor
@Getter
@Setter
public class OrderEvent {

    private Long orderId;
    private Long userId;
    private BigDecimal amount;
    private Date createdAt;
}
