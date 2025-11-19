package com.order.service.kafka;

import com.order.service.model.enums.OrderStatus;
import com.order.service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentCreatedConsumer {

    private final OrderService orderService;
    private final KafkaTemplate<String, Object> kafkaTemplate;


    @KafkaListener(topics = "order-payment-complete", groupId = "order-service")
    public void consumePaymentProcessed(PaymentEvent event) throws Exception {
        try {
            orderService.updateOrderStatus(event.getOrderId(), OrderStatus.valueOf(event.getStatus()));
        } catch (Exception e) {
            throw new Exception("Failed to process event");
        }
    }
}
