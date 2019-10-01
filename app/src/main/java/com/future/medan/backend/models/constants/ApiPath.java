package com.future.medan.backend.models.constants;

public class ApiPath {

    public static final String API = "/api";

    public static final String PRODUCTS = API + "/products";
    public static final String PRODUCT_BY_PRODUCT_ID = PRODUCTS + "/{id}";

    public static final String USERS = API + "/users";
    public static final String USER_BY_USER_ID = USERS + "/{id}";

    public static final String CATEGORIES = API + "/categories";
    public static final String CATEGORY_BY_CATEGORY_ID = CATEGORIES + "/{id}";
}
