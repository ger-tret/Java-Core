package com.payment.service.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "payments")
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long paymentId;

    private Long orderId;
    private Long userId;

    private PaymentStatus status;

    private Date timestamp;
    private BigDecimal paymentAmount;
}

