package com.auth.service.model.enums;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

public enum UserRole implements GrantedAuthority {
    USER, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }

    public GrantedAuthority toAuthority() {
        return new SimpleGrantedAuthority(this.name());
    }

    // Convert list of UserRole to List<GrantedAuthority>
    public static List<GrantedAuthority> toAuthorities(List<UserRole> roles) {
        return roles.stream()
                .map(UserRole::toAuthority)
                .collect(Collectors.toList());
    }
}
