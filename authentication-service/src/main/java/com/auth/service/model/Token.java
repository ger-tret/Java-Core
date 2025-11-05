package com.auth.service.model;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.util.StringUtils;

@Data
@Builder
public class Token {

    private String accessToken;
    private Long accessTokenExpiresAt;
    private String refreshToken;

    private static final String TOKEN_PREFIX = "Bearer ";

    /**
     * Checks if the given authorization header contains a Bearer token.
     *
     * @param authorizationHeader the Authorization header value
     * @return {@code true} if the header starts with "Bearer "; {@code false} otherwise
     */
    public static boolean isBearerToken(final String authorizationHeader) {
        return StringUtils.hasText(authorizationHeader) &&
                authorizationHeader.startsWith(TOKEN_PREFIX);
    }

    public static String getJwt(final String authorizationHeader) {
        return authorizationHeader.replace(TOKEN_PREFIX, "");
    }

}
