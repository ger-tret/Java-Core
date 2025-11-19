package com.payment.service.kafka.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kafka")
@Data
public class KafkaProperties {
    private String bootstrapServers;
    private String consumerGroupId;
    private Topics topics = new Topics();

    @Data
    public static class Topics {
        private String orderCreated;
        private String paymentCreated;
        private String paymentFailed;
    }
}