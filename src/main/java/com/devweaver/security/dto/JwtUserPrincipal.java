package com.devweaver.security.dto;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public record JwtUserPrincipal(
        Long userId,
        String username,
        List<GrantedAuthority> authorities
) {
}
