package com.future.medan.backend.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationApiResponse {

    private Boolean success;

    private String message;
}
