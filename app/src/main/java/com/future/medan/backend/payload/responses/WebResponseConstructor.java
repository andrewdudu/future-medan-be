package com.future.medan.backend.payload.responses;

import com.future.medan.backend.models.entity.*;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class WebResponseConstructor {

    public static ProductWebResponse toWebResponse(Product product) {
        return ProductWebResponse.builder()
                .id(product.getId())
                .sku(product.getSku())
                .variant(product.getVariant())
                .name(product.getName())
                .author(product.getAuthor())
                .description(product.getDescription())
                .isbn(product.getIsbn())
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

    public static WishlistWebResponse toWebResponse(Wishlist wishlist) {
        Set<Product> products = (Set<Product>) Hibernate.unproxy(wishlist.getProducts());
        Set<ProductWebResponse> productWebResponses = products
                .stream()
                .map(WebResponseConstructor::toWebResponse)
                .collect(Collectors.toSet());

        UserWebResponse userWebResponse = WebResponseConstructor.toWebResponse((User) Hibernate.unproxy(wishlist.getUser()));

        return WishlistWebResponse.builder()
                .user(userWebResponse)
                .products(productWebResponses)
                .build();
    }

    public static PurchaseWebResponse toWebResponse(Purchase purchase){
        User merchant = (User) Hibernate.unproxy(purchase.getMerchant());
        User user = (User) Hibernate.unproxy(purchase.getUser());
        Product product = (Product) Hibernate.unproxy(purchase.getProduct());

        UserWebResponse merchantWebResponse = WebResponseConstructor.toWebResponse(merchant);
        UserWebResponse userWebResponse = WebResponseConstructor.toWebResponse(user);
        ProductWebResponse productWebResponse = WebResponseConstructor.toWebResponse(product);

        return PurchaseWebResponse.builder()
                .merchant(merchantWebResponse)
                .order_id(purchase.getOrderId())
                .product(productWebResponse)
                .status(purchase.getStatus())
                .user(userWebResponse)
                .build();
    }

    public static AuthenticationApiResponse toValidateToken(Boolean success) {
        return AuthenticationApiResponse.builder()
                .message(null)
                .success(success)
                .build();
    }

    public static CartWebResponse toWebResponse(Cart cart) {
        Set<ProductWebResponse> productWebResponses = cart.getProducts()
                .stream()
                .map(WebResponseConstructor::toWebResponse)
                .collect(Collectors.toSet());

        User user = (User) Hibernate.unproxy(cart.getUser());

        UserWebResponse userWebResponse = WebResponseConstructor.toWebResponse(user);

        return CartWebResponse.builder()
                .products(productWebResponses)
                .user(userWebResponse)
                .build();
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

    public static MerchantWebResponse toWebResponse(User merchant, List<Product> products) {
        List<ProductWebResponse> productWebResponses = products
                .stream()
                .map(WebResponseConstructor::toWebResponse)
                .collect(Collectors.toList());

        UserWebResponse merchantWebResponse = WebResponseConstructor.toWebResponse(merchant);

        return MerchantWebResponse.builder()
                .products(productWebResponses)
                .user(merchantWebResponse)
                .build();
    }

    public static FileWebResponse toFileWebResponse(String base64) {
        return FileWebResponse.builder()
                .base64(base64)
                .build();
    }

    public static PaymentMethodWebResponse toPaymentMethodWebResponse (PaymentMethod paymentMethod) {
        return PaymentMethodWebResponse.builder()
                .id(paymentMethod.getId())
                .name(paymentMethod.getName())
                .type(paymentMethod.getType())
                .active(paymentMethod.getActive())
                .build();
    }

    public static ProductByIdWebResponse toProductByIdWebResponse(Product product) {
        User merchant = (User) Hibernate.unproxy(product.getMerchant());

        UserWebResponse merchantWebResponse = WebResponseConstructor.toWebResponse(merchant);

        return ProductByIdWebResponse.builder()
                .id(product.getId())
                .sku(product.getSku())
                .variant(product.getVariant())
                .name(product.getName())
                .author(product.getAuthor())
                .description(product.getDescription())
                .image(product.getImage())
                .price(product.getPrice())
                .pdf(product.getPdf())
                .hidden(product.getHidden())
                .merchant(merchantWebResponse)
                .build();
    }

    public static ReviewWebResponse toReviewEntity(Review review) {

        UserWebResponse userWebResponse = WebResponseConstructor.toWebResponse((User) Hibernate.unproxy(review.getUser()));
        ProductWebResponse productWebResponse = WebResponseConstructor.toWebResponse((Product) Hibernate.unproxy(review.getProduct()));

        return ReviewWebResponse.builder()
                .comment(review.getComment())
                .product(productWebResponse)
                .rating(review.getRating())
                .user(userWebResponse)
                .build();
    }
}
