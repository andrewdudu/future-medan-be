package com.future.medan.backend.payload.requests;

import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.models.entity.Purchase;

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

    public static Purchase toPurchaseEntity(PurchaseWebRequest purchaseWebRequest) {
        return Purchase.builder()
                .author_name(purchaseWebRequest.getAuthor_name())
                .price(purchaseWebRequest.getPrice())
                .product_description(purchaseWebRequest.getProduct_description())
                .product_image(purchaseWebRequest.getProduct_image())
                .product_name(purchaseWebRequest.getProduct_name())
                .product_sku(purchaseWebRequest.getProduct_sku())
                .qty(purchaseWebRequest.getQty())
                .build();
    }
}
