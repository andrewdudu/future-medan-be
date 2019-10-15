package com.future.medan.backend.payload.requests;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class ForgotPasswordRequest {

    @Email
    private String email;
}
