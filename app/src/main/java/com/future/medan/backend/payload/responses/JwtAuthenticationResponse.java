package com.future.medan.backend.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthenticationResponse {

    private String accessToken;

    private Collection<? extends GrantedAuthority> roles;

    private String tokenType = "Bearer";

    private String id;

    private String username;

    private String name;

    private String email;

    public JwtAuthenticationResponse(String id, String name, String username, String email, String accessToken, Collection<? extends GrantedAuthority> roles) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.accessToken = accessToken;
        this.roles = roles;
    }
}