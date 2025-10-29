package com.api.gateway.service.filter;

import com.api.gateway.service.exception.JWTTokenExtractException;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractNameValueGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
@Component
public class JWTFilter extends AbstractNameValueGatewayFilterFactory {

    private static final String X_JWT_SUB_HEADER = "X-jwt-sub";
    private final JWTVerifier jwtVerifier;

    public JWTFilter(@Qualifier("jwk") JWTVerifier jwtVerifier) {
        this.jwtVerifier = jwtVerifier;
    }

    @Override
    public GatewayFilter apply(NameValueConfig config) {
        return (exchange, chain) -> {

            try {
                String token = this.extractJWTToken(exchange.getRequest());
                DecodedJWT decodedJWT = this.jwtVerifier.verify(token);

                ServerHttpRequest request = exchange.getRequest().mutate().
                        header(X_JWT_SUB_HEADER, decodedJWT.getSubject()).
                        build();

                return chain.filter(exchange.mutate().request(request).build());

            } catch (JWTVerificationException _) {

                return this.onError(exchange);
            }
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange)
    {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);

        return response.setComplete();
    }

    private String extractJWTToken(ServerHttpRequest request)
    {
        if (!request.getHeaders().containsHeader("Authorization")) {
            throw new JWTTokenExtractException("Authorization header is missing");
        }

        List<String> headers = request.getHeaders().get("Authorization");
        if (headers.isEmpty()) {
            throw new JWTTokenExtractException("Authorization header is empty");
        }

        String credential = headers.get(0).trim();
        String[] components = credential.split("\\s");

        if (components.length != 2) {
            throw new JWTTokenExtractException("Malformat Authorization content");
        }

        if (!components[0].equals("Bearer")) {
            throw new JWTTokenExtractException("Bearer is needed");
        }

        return components[1].trim();
    }

}