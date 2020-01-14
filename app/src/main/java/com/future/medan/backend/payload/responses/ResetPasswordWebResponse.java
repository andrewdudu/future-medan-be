package com.future.medan.backend.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class ResetPasswordWebResponse {

    private boolean success;
}
