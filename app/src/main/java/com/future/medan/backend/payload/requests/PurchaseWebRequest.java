package com.future.medan.backend.payload.requests;


import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
public class PurchaseWebRequest {

    @NotEmpty
    private Set<String> products;
}
