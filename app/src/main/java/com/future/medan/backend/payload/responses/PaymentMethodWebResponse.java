package com.future.medan.backend.payload.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentMethodWebResponse {

    private String id;

    private String name;

    private String type;

    private Boolean active;
}
