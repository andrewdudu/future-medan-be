package com.future.medan.backend.payload.requests;

import com.future.medan.backend.models.entity.Product;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
public class WishlistWebRequest {

    @NotBlank
    private String user_id;

    @NotEmpty
    private Set<Product> product_id;
}
