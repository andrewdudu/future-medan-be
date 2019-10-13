package com.future.medan.backend.payload.requests;

import lombok.Data;

@Data
public class PasswordResetRequest {

    private String token;

    private String password;
}
