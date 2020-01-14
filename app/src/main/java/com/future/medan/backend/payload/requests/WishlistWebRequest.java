package com.future.medan.backend.payload.requests;

import com.future.medan.backend.models.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishlistWebRequest {

    @NotEmpty
    private String product_id;
}
