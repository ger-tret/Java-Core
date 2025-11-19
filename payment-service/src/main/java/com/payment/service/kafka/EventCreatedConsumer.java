package com.payment.service.kafka;

import com.payment.service.model.Payment;
import com.payment.service.model.PaymentStatus;
import com.payment.service.model.dto.CreatePaymentDto;
import com.payment.service.model.dto.OrderEvent;
import com.payment.service.model.dto.PaymentEvent;
import com.payment.service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventCreatedConsumer {

    private final PaymentService paymentService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String PAYMENT_COMPLETE_TOPIC = "payment-processed";
    private static final String PAYMENT_FAILED_TOPIC = "payment-failed";

    @KafkaListener(topics = "order-created-topic", groupId = "payment-service")
    public void handleOrderCreatedEvent(OrderEvent event) {
        try {
            CreatePaymentDto createPaymentDto = new CreatePaymentDto();
            createPaymentDto.setOrderId(event.getOrderId());
            createPaymentDto.setUserId(Long.valueOf(event.getUserId()));
            createPaymentDto.setPaymentAmount(event.getAmount());

            Payment payment = paymentService.createPayment(createPaymentDto);

            PaymentEvent paymentEvent = new PaymentEvent(event.getOrderId(), payment.getPaymentId(), "SUCCESS");
            kafkaTemplate.send(PAYMENT_COMPLETE_TOPIC, String.valueOf(event.getOrderId()),paymentEvent);
        } catch (Exception e) {
            kafkaTemplate.send(PAYMENT_FAILED_TOPIC, String.valueOf(event.getOrderId()),event);
        }
    }
}