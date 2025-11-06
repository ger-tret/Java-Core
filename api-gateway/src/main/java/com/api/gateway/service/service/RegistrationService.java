package com.api.gateway.service.service;

import com.api.gateway.service.dto.CommitResponseDto;
import com.api.gateway.service.dto.CompensationRequestDto;
import com.api.gateway.service.dto.RegisterRequestDto;
import com.api.gateway.service.dto.RegistrationResponse;
import com.api.gateway.service.exception.SagaException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegistrationService {

    private final WebClient.Builder webClientBuilder;
    private static final String SAGA_ID = "X-Saga-id";
    private final ObjectMapper objectMapper;

    public Mono<RegistrationResponse> executeRegistrationSaga(RegisterRequestDto request) {
        String sagaId = "saga_" + UUID.randomUUID().toString().substring(0, 8);
        log.info("Starting registration: {} for user: {}", sagaId, request.getEmail());

        return Mono.just(request)
                .flatMap(req -> registerInAuthService(req, sagaId))
                .doOnSuccess(authResponse -> log.info("SAGA {}: Auth registration successful", sagaId))

                .flatMap(authResponse -> registerInUserService(request, sagaId))
                .doOnSuccess(userResponse -> log.info("SAGA {}: User registration successful", sagaId))

                .flatMap(userResponse -> commitSaga(sagaId, userResponse.getUserId()))
                .doOnSuccess(CommitResponseDto -> log.info("SAGA {}: Commit successful", sagaId))

                .map(CommitResponseDto -> RegistrationResponse.success(
                        CommitResponseDto.getUserId(),
                        "User registered successfully"
                ))

                .onErrorResume(error -> {
                    log.error("SAGA {} failed: {}", sagaId, error.getMessage());
                    return compensateSaga(sagaId, request.getEmail(), error)
                            .then(Mono.just(RegistrationResponse.failed(
                                    "Registration failed: " + error.getMessage()
                            )));
                })
                .doOnTerminate(() -> log.info("SAGA {} completed", sagaId));
    }

    private Mono<RegistrationResponse> registerInAuthService(RegisterRequestDto request, String sagaId) {
        log.debug("SAGA {}: Registering in Auth Service", sagaId);
        return webClientBuilder.build()
                .post()
                .uri("lb://auth/register")
                .header(SAGA_ID, sagaId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        handleAuthServiceError(response, "Auth service client error"))
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        handleAuthServiceError(response, "Auth service server error"))
                .bodyToMono(RegistrationResponse.class)
                .timeout(Duration.ofSeconds(10))
                .doOnError(error -> {
                    throw new SagaException("Auth service registration failed: " + error.getMessage());
                });
    }

    private Mono<RegistrationResponse> registerInUserService(RegisterRequestDto request, String sagaId) {
        log.debug("SAGA {}: Registering in User Service", sagaId);

        return webClientBuilder.build()
                .post()
                .uri("lb://users/register")
                .header(SAGA_ID, sagaId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        handleUserServiceError(response, "User service client error"))
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        handleUserServiceError(response, "User service server error"))
                .bodyToMono(RegistrationResponse.class)
                .timeout(Duration.ofSeconds(10))
                .doOnError(error -> {
                    throw new SagaException("User service registration failed: " + error.getMessage());
                });
    }

    private Mono<CommitResponseDto> commitSaga(String sagaId, Long userId) {
        log.debug("SAGA {}: Committing saga for user ID: {}", sagaId, userId);

        // Commit both services in parallel
        Mono<Void> commitAuth = webClientBuilder.build()
                .post()
                .uri("lb://auth-service/api/auth/sagas/{sagaId}/commit", sagaId)
                .header(SAGA_ID, sagaId)
                .retrieve()
                .toBodilessEntity()
                .then();

        Mono<Void> commitUser = webClientBuilder.build()
                .post()
                .uri("lb://user-service/api/users/sagas/{sagaId}/commit", sagaId)
                .header(SAGA_ID, sagaId)
                .retrieve()
                .toBodilessEntity()
                .then();

        // Execute both commits in parallel and wait for completion
        return Mono.zip(commitAuth, commitUser)
                .timeout(Duration.ofSeconds(5))
                .thenReturn(CommitResponseDto.success(userId, sagaId))
                .doOnError(error -> {
                    throw new SagaException("Saga commit failed: " + error.getMessage());
                });
    }

    private Mono<Void> compensateSaga(String sagaId, String email, Throwable error) {
        log.warn("SAGA {}: Compensating saga due to error: {}", sagaId, error.getMessage());

        CompensationRequestDto compensationRequestDto = CompensationRequestDto.builder()
                .sagaId(sagaId)
                .email(email)
                .reason(error.getMessage())
                .timestamp(Instant.now())
                .build();

        // Execute compensation in reverse order (User Service first, then Auth Service)
        return compensateUserService(compensationRequestDto)
                .then(compensateAuthService(compensationRequestDto))
                .doOnSuccess(v -> log.info("SAGA {}: Compensation completed", sagaId))
                .doOnError(compError ->
                        log.error("SAGA {}: Compensation failed: {}", sagaId, compError.getMessage()));
    }

    private Mono<Void> compensateUserService(CompensationRequestDto request) {
        return webClientBuilder.build()
                .post()
                .uri("lb://user-service/api/users/sagas/compensate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .toBodilessEntity()
                .then()
                .doOnSuccess(v -> log.debug("User service compensation successful for SAGA: {}", request.getSagaId()))
                .onErrorResume(error -> {
                    log.error("User service compensation failed for SAGA {}: {}", request.getSagaId(), error.getMessage());
                    return Mono.empty(); // Continue with auth compensation even if user compensation fails
                });
    }

    private Mono<Void> compensateAuthService(CompensationRequestDto request) {
        return webClientBuilder.build()
                .post()
                .uri("lb://auth-service/api/auth/sagas/compensate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .toBodilessEntity()
                .then()
                .doOnSuccess(v -> log.debug("Auth service compensation successful for SAGA: {}", request.getSagaId()))
                .onErrorResume(error -> {
                    log.error("Auth service compensation failed for SAGA {}: {}", request.getSagaId(), error.getMessage());
                    return Mono.empty();
                });
    }

    // Error handlers
    private Mono<? extends Throwable> handleAuthServiceError(ClientResponse response, String message) {
        return response.bodyToMono(String.class)
                .flatMap(body -> {
                    log.warn("Auth Service error: {} - {}", message, body);
                    return Mono.error(new SagaException("Auth Service: " + body));
                });
    }

    private Mono<? extends Throwable> handleUserServiceError(ClientResponse response, String message) {
        return response.bodyToMono(String.class)
                .flatMap(body -> {
                    log.warn("User Service error: {} - {}", message, body);
                    return Mono.error(new SagaException("User Service: " + body));
                });
    }
}
