package com.auth.service.security;

import com.auth.service.exception.HttpException;
import com.auth.service.services.JwtTokenServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtTokenServiceImpl jwtTokenService;

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

}