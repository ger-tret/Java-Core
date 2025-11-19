package com.payment.service.service.mapper;

import com.payment.service.model.Payment;
import com.payment.service.model.dto.CreatePaymentDto;
import com.payment.service.model.dto.PaymentDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentDto toDto(Payment payment);
    Payment toEntity(PaymentDto paymentDto);


    Payment toEntity(CreatePaymentDto createPaymentDto);

    List<PaymentDto> toDtos(List<Payment> paymentList);
    List<Payment> toEntities(List<PaymentDto> paymentDtoList);
}