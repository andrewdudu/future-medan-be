package com.future.medan.backend.payload.requests;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
public class CartWebRequest {

    @NotEmpty
    private String product_id;
}
