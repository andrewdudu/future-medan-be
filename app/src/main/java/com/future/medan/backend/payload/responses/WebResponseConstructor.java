package com.future.medan.backend.payload.responses;

import com.future.medan.backend.models.entity.*;

public class WebResponseConstructor {

    public static ProductWebResponse toWebResponse(Product product) {
        return ProductWebResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .sku(product.getSku())
                .author(product.getAuthor())
                .description(product.getDescription())
                .image(product.getImage())
                .price(product.getPrice())
                .pdf(product.getPdf())
                .hidden(product.getHidden())
                .build();
    }

    public static UserWebResponse toWebResponse(User user) {
        return UserWebResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .description(user.getDescription())
                .email(user.getEmail())
                .username(user.getUsername())
                .image(user.getImage())
                .status(user.getStatus())
                .build();
    }

    public static CategoryWebResponse toWebResponse(Category category){
        return CategoryWebResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .image(category.getImage())
                .hidden(category.getHidden())
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

    public static AuthenticationApiResponse toValidateToken(Boolean success) {
        return AuthenticationApiResponse.builder()
                .message(null)
                .success(success)
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

    public static FileWebResponse toFileWebResponse(String base64) {
        return FileWebResponse.builder()
                .base64(base64)
                .build();
    }
}
