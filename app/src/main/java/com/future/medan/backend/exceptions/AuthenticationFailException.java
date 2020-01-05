package com.future.medan.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationFailException extends RuntimeException {

    public AuthenticationFailException(String message) {
        super(message);
    }
}
