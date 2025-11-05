package com.order.service.client;


import com.user.service.model.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", url = "${user.service.url:http://localhost:8080}")
public interface UserServiceClient {

    @GetMapping("/api/users/email")
    UserDto getUserByEmail(@RequestParam String email);
}
