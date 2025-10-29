package com.api.gateway.service.controller;

import com.api.gateway.service.service.RegistrationService;
import com.api.gateway.service.dto.RegisterRequestDto;
import com.api.gateway.service.dto.RegistrationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/gateway")
@RequiredArgsConstructor
@Slf4j
public class GatewayRegistrationController {

    private final RegistrationService sagaService;

    @PostMapping("/register")
    public Mono<ResponseEntity<RegistrationResponse>> registerUser(
            @Valid @RequestBody RegisterRequestDto request) {

        log.info("Received SAGA registration request for: {}", request.getEmail());

        return sagaService.executeRegistrationSaga(request)
                .map(response -> {
                    if (response.isSuccess()) {
                        return ResponseEntity.status(HttpStatus.CREATED).body(response);
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                    }
                })
                .onErrorResume(error -> {
                    log.error("Registration failed unexpectedly: {}", error.getMessage());
                    RegistrationResponse errorResponse = RegistrationResponse.failed(
                            "Registration failed due to system error");
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(errorResponse));
                });
    }
}