package com.future.medan.backend.payload.responses;

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

    private String id;

    private String name;

    private String sku;

    private String variant;

    private String description;

    private String ISBN;

    private BigDecimal price;

    private String image;

    private String pdf;

    private String author;

    private Boolean hidden;
}
