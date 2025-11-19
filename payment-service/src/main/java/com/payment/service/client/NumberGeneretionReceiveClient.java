package com.payment.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name="randomnumberapi", url="${https://www.randomnumberapi.com/api/v1.0/random?min=100&max=1000&count=1}")
public interface NumberGeneretionReceiveClient {
    @GetMapping
    public Integer getPaymentStatus();
}
