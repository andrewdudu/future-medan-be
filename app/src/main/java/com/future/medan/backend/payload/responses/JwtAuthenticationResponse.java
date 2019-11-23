package com.future.medan.backend.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
public class JwtAuthenticationResponse {

    private String accessToken;

    private Collection<? extends GrantedAuthority> roles;

    private String tokenType = "Bearer";

    public JwtAuthenticationResponse(String accessToken, Collection<? extends GrantedAuthority> roles) {
        this.accessToken = accessToken;
        this.roles = roles;
    }
}