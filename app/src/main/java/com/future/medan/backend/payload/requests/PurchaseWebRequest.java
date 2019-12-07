package com.future.medan.backend.payload.requests;

import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.models.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class PurchaseWebRequest {

    @NotBlank
    private String product_id;
}
