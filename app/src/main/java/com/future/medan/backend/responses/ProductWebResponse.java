package com.future.medan.backend.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductWebResponse {

    private String name;

    private String sku;

    private String description;

    private BigDecimal price;

    private String image;

    private String author;
}
