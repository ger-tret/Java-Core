package com.auth.service.controller;

import com.auth.service.model.dto.request.RegisterRequestDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", url = "${user.service.url:http://localhost:8080}")
public interface UserServiceRecieveClient {
    @PostMapping("/api/users/")
    Long createUser(@RequestBody @Valid RegisterRequestDto registerRequestDto);
}
