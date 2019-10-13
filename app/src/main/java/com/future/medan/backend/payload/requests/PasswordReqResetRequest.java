package com.future.medan.backend.payload.requests;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class PasswordReqResetRequest {

    @Email
    private String email;
}
