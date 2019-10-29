package com.future.medan.backend.payload.responses;

import com.future.medan.backend.models.entity.*;

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

    public static UserWebResponse toWebResponse(User user) {
        return UserWebResponse.builder()
                .name(user.getName())
                .description(user.getDescription())
                .email(user.getEmail())
                .username(user.getUsername())
                .image(user.getImage())
                .password(user.getPassword())
                .build();
    }

    public static CategoryWebResponse toWebResponse(Category category){
        return CategoryWebResponse.builder()
                .parent_id(category.getParent_id())
                .name(category.getName())
                .description(category.getDescription())
                .image(category.getImage())
                .build();
    }

    public static WishlistWebResponse toWebResponse(Wishlist wishlist){
        return WishlistWebResponse.builder().build();
    }

    public static PurchaseWebResponse toWebResponse(Purchase purchase){
        return PurchaseWebResponse.builder()
                .price(purchase.getPrice())
                .productName(purchase.getProduct_name())
                .productDescription(purchase.getProduct_description())
                .productSku(purchase.getProduct_sku())
                .productImage(purchase.getProduct_image())
                .authorName(purchase.getAuthor_name())
                .build();
    }

    public static CartWebResponse toWebResponse(Cart cart){
        return CartWebResponse.builder().build();
    }

    public static ForgotPasswordWebResponse toForgotPasswordWebResponse(boolean status) {
        return ForgotPasswordWebResponse.builder()
                .success(status)
                .build();
    }

    public static ResetPasswordWebResponse toResetPasswordWebResponse(boolean status) {
        return ResetPasswordWebResponse.builder()
                .success(status)
                .build();
    }
}
