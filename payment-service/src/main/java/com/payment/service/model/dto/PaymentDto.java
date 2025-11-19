package com.payment.service.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PaymentDto {
    private Long paymentId;
    private Long orderId;
    private Long userId;

    private String status;

    private Date timestamp;
    private BigDecimal paymentAmount;
}
