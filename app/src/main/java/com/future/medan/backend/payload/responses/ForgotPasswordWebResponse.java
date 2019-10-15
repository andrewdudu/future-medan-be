package com.future.medan.backend.payload.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ForgotPasswordWebResponse {

    private boolean success;
}
