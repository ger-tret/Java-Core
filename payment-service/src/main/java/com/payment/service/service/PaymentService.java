package com.payment.service.service;

import com.payment.service.model.Payment;
import com.payment.service.model.dto.CreatePaymentDto;
import com.payment.service.model.dto.PaymentDto;

import java.util.Date;
import java.util.List;

public interface PaymentService {
    Payment createPayment(CreatePaymentDto createPaymentDto);
    PaymentDto getPaymentByPaymentId(Long paymentId);
    PaymentDto getPaymentByOrderId(Long orderId);
    PaymentDto getPaymentByUserId(Long userId);
    List<PaymentDto> getPaymentByStatus(String status);
    Long updatePaymentStatus(Long id, String status);
    Integer getPaymentSumByDatePeriod(Date periodStart, Date periodEnd);
    Long deletePayment(Long id);
    List<PaymentDto> getAllPayments();
}
