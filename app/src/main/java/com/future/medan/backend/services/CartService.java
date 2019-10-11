package com.future.medan.backend.services;

import com.future.medan.backend.models.entity.Cart;

import java.util.List;

public interface CartService {

    List<Cart> getAll();

    Cart getById(String id);

    Cart save(Cart cart);

    Cart save(Cart cart, String id);

    void deleteById(String id);
}
