package com.future.medan.backend.payload.requests;

import com.future.medan.backend.models.entity.Product;

public class WebRequestConstructor {

    public static Product toProductEntity(ProductWebRequest productWebRequest) {
        return Product.builder()
                .name(productWebRequest.getName())
                .author(productWebRequest.getAuthor())
                .description(productWebRequest.getDescription())
                .image(productWebRequest.getImage())
                .price(productWebRequest.getPrice())
                .sku(productWebRequest.getSku())
                .pdf(productWebRequest.getPdf())
                .build();
    }
}
