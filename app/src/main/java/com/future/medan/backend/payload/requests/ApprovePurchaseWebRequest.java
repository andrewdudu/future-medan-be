package com.future.medan.backend.payload.requests;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ApprovePurchaseWebRequest {

    @NotEmpty
    private String product_id;

    @NotEmpty
    private String order_id;
}
