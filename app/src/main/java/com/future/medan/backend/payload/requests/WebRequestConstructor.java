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
                .isbn(productWebRequest.getIsbn())
                .build();
    }

    public static Purchase toPurchaseEntity(PurchaseWebRequest purchaseWebRequest) {
        return Purchase.builder()
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

    public static Review toReviewEntity(ReviewWebRequest reviewWebRequest, User user, Product product) {
        return Review.builder()
                .comment(reviewWebRequest.getComment())
                .rating(reviewWebRequest.getRating())
                .product(product)
                .user(user)
                .build();
    }

    public static Cart toCartEntity(User user, Set<Product> products) {
        return Cart.builder()
                .user(user)
                .products(products)
                .build();
    }
}
