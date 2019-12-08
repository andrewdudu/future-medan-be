package com.future.medan.backend.payload.requests;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PaymentWebRequest {

    @NotBlank
    private String order_id;
}
