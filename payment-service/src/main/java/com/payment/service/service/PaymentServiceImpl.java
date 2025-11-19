package com.payment.service.service;

import com.payment.service.client.NumberGeneretionReceiveClient;
import com.payment.service.model.Payment;
import com.payment.service.model.PaymentStatus;
import com.payment.service.model.dto.CreatePaymentDto;
import com.payment.service.model.dto.PaymentDto;
import com.payment.service.repository.PaymentRepository;
import com.payment.service.service.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final NumberGeneretionReceiveClient numberGeneretionReceiveClient;
    private final PaymentMapper paymentMapper;

    @Override
    public Payment createPayment(CreatePaymentDto createPaymentDto) {
        Payment payment = paymentMapper.toEntity(createPaymentDto);
        if(numberGeneretionReceiveClient.getPaymentStatus() % 2 == 0){
            payment.setStatus(PaymentStatus.SUCCESS);
        } else payment.setStatus(PaymentStatus.FAILED);
        return paymentRepository.save(payment);
    }

    @Override
    public PaymentDto getPaymentByPaymentId(Long paymentId) {
        return paymentMapper.toDto(paymentRepository.findById(paymentId).orElseThrow(() -> new NoSuchElementException("Payment not found")));
    }

    @Override
    public PaymentDto getPaymentByOrderId(Long orderId) {
        return paymentMapper.toDto(paymentRepository.findByOrderId(orderId));
    }

    @Override
    public PaymentDto getPaymentByUserId(Long userId) {
        return paymentMapper.toDto(paymentRepository.findByUserId(userId));
    }

    @Override
    public List<PaymentDto> getPaymentByStatus(String status) {
        return paymentMapper.toDtos(paymentRepository.findAllByStatus(status));
    }

    @Override
    public Integer getPaymentSumByDatePeriod(Date periodStart, Date periodEnd) {
        return paymentRepository.getSumByPeriod(periodStart, periodEnd);
    }
    @Override
    public Long deletePayment(Long id){
        paymentRepository.deleteById(id);
        return id;
    }

    @Override
    public Long updatePaymentStatus(Long id, String status){
        return paymentRepository.updateStatus(id, status);
    }
    @Override
    public List<PaymentDto> getAllPayments(){
        return paymentMapper.toDtos(paymentRepository.findAll());
    }
}
