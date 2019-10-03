package com.future.medan.backend.payload.responses;

import com.future.medan.backend.models.entity.Product;

public class WebResponseConstructor {

    public static ProductWebResponse toWebResponse(Product product) {
        return ProductWebResponse.builder()
                .name(product.getName())
                .sku(product.getSku())
                .author(product.getAuthor())
                .description(product.getDescription())
                .image(product.getImage())
                .price(product.getPrice())
                .build();
    }
}
