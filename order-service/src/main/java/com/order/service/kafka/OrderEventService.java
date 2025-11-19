package com.order.service.kafka;

import com.order.service.model.enums.OrderStatus;
import com.order.service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class OrderEventService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String ORDER_CREATED_TOPIC = "order-created";

    public void sendOrderCreatedEvent(Long orderId, Long userId, BigDecimal amount,
                                      Date createdAt) {
        OrderEvent event = new OrderEvent(orderId, userId, amount, createdAt);
        kafkaTemplate.send(ORDER_CREATED_TOPIC, String.valueOf(orderId), event);
    }

}