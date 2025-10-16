package com.auth.service.security;

import com.auth.service.exception.HttpException;
import com.auth.service.services.JwtTokenServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenServiceImpl jwtTokenService;

    public JwtAuthenticationFilter(JwtTokenServiceImpl jwtTokenProvider) {
        this.jwtTokenService = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException, ServletException, IOException {
        String token = jwtTokenService.resolveToken(httpServletRequest);
        try {
            if (token != null && jwtTokenService.validateToken(token)) {
                Authentication auth = jwtTokenService.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (HttpException ex) {
            SecurityContextHolder.clearContext();
            httpServletResponse.sendError(ex.getHttpStatus().value(), ex.getMessage());
            return;
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;

    }
}