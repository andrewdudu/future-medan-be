package com.future.medan.backend.payload.requests;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginWebRequest {

    @NotBlank
    private String usernameOrEmail;

    @NotBlank
    private String password;
}
