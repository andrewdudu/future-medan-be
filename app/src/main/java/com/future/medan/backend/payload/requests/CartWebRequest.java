package com.future.medan.backend.payload.requests;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
public class CartWebRequest {

    @NotBlank
    private String user_id;

    @NotEmpty
    private Set<String> product_id;
}
