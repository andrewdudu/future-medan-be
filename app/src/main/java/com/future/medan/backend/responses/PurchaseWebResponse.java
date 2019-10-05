package com.future.medan.backend.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseWebResponse {

    private BigDecimal price;

    private String productName;

    private String productDescription;

    private String productSku;

    private String productImage;

    private String authorName;
}
