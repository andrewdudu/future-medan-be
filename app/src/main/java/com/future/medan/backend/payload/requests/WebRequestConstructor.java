package com.future.medan.backend.payload.requests;

import com.future.medan.backend.models.entity.*;

import java.util.Set;

public class WebRequestConstructor {

    public static Product toProductEntity(ProductWebRequest productWebRequest) {
        return Product.builder()
                .name(productWebRequest.getName())
                .author(productWebRequest.getAuthor())
                .description(productWebRequest.getDescription())
                .image(productWebRequest.getImage())
                .price(productWebRequest.getPrice())
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

    public static Category toCategoryEntity(CategoryWebRequest categoryWebRequest) {
        return Category.builder()
                .description(categoryWebRequest.getDescription())
                .name(categoryWebRequest.getName())
                .image(categoryWebRequest.getImage())
                .hidden(categoryWebRequest.getHidden())
                .build();
    }

    public static Cart toCartEntity(User user, Set<Product> products) {
        return Cart.builder()
                .user(user)
                .products(products)
                .build();
    }
}
