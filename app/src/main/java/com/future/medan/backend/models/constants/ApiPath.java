package com.future.medan.backend.models.constants;

public class ApiPath {

    public static final String API = "/api";

    public static final String PRODUCTS = API + "/products";
    public static final String PRODUCT_BY_PRODUCT_ID = PRODUCTS + "/{id}";

    public static final String USERS = API + "/users";
    public static final String USER_BY_USER_ID = USERS + "/{id}";

    public static final String CATEGORIES = API + "/categories";
    public static final String CATEGORY_BY_CATEGORY_ID = CATEGORIES + "/{id}";

    public static final String ROLES = API + "/roles";

    public static final String WISHLISTS = API + "/wishlists";
    public static final String WISHLIST_BY_WISHLIST_ID = WISHLISTS + "/{id}";

    public static final String PURCHASES = API + "/purchases";
    public static final String PURCHASE_BY_PURCHASE_ID = PURCHASES + "/{id}";
}
