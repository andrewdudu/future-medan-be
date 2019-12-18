package com.future.medan.backend.services;

import com.future.medan.backend.models.entity.Cart;
import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.models.entity.User;

import java.util.List;

public interface CartService {

    List<Cart> getAll();

    Cart getById(String id);

    Cart getByUserId(String user_id);

    Cart save(User user, Product product);

    Cart save(Cart cart, String id);

    Cart deleteByProductId(String productId, String userId);
}
