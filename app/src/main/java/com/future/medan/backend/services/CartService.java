package com.future.medan.backend.services;

import com.future.medan.backend.models.entity.Cart;

import java.util.List;
import java.util.Optional;

public interface CartService {

    List<Cart> getAll();

    Optional<Cart> getById(String id);

    Cart save(Cart cart);

    void deleteById(String id);
}
