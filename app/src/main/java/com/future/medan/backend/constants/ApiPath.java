package com.future.medan.backend.constants;

public class ApiPath {

    public static final String API = "/api";

    public static final String PRODUCTS = API + "/products";
    public static final String PRODUCT_BY_PRODUCT_ID = PRODUCTS + "/{id}";

    public static final String MERCHANT_REGISTER = API + "/merchant/register";
    public static final String USER_REGISTER = API + "/user/register";
    public static final String LOGIN = API + "/login";
    public static final String LOGOUT = API + "/logout";

    public static final String USERS = API + "/users";
    public static final String USER_BY_USER_ID = USERS + "/{id}";

    public static final String CATEGORIES = API + "/categories";
    public static final String CATEGORY_BY_CATEGORY_ID = CATEGORIES + "/{id}";

    public static final String ROLES = API + "/roles";

    public static final String WISHLISTS = API + "/wishlists";
    public static final String WISHLIST_BY_WISHLIST_ID = WISHLISTS + "/{id}";

    public static final String PURCHASES = API + "/purchases";
    public static final String PURCHASE_BY_PURCHASE_ID = PURCHASES + "/{id}";

    public static final String CARTS = API + "/carts";
    public static final String CART_BY_CART_ID = CARTS + "/{id}";

    public static final String VALIDATE_TOKEN = API + "/validate-token";
}
