package com.future.medan.backend.payload.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApprovePurchaseWebRequest {

    @NotEmpty
    private String product_id;

    @NotEmpty
    private String order_id;
}
