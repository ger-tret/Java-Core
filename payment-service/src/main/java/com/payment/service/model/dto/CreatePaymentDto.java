package com.payment.service.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CreatePaymentDto {
    @NotNull
    private Long orderId;
    @NotNull
    private Long userId;
    @NotNull
    private Date timestamp;
    private BigDecimal paymentAmount;
}

