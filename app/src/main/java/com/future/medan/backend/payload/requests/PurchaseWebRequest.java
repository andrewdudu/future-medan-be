package com.future.medan.backend.payload.requests;

import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.models.entity.User;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
public class PurchaseWebRequest {

    @NotNull
    @DecimalMin(value = "0", inclusive = true)
    private BigDecimal price;

    @NotBlank
    private String author_name;

    @NotBlank
    private String product_description;

    @NotBlank
    private String product_image;

    @NotBlank
    private String product_name;

    @NotBlank
    private String product_sku;

    @NotNull
    @DecimalMin(value = "0", inclusive = true)
    private Integer qty;

    @NotBlank
    private String user;

    @NotBlank
    private String product;

}
