package com.future.medan.backend.services;

import com.future.medan.backend.models.entity.Wishlist;

import java.util.List;
import java.util.Optional;

public interface WishlistService {

    List<Wishlist> getAll();

    Optional<Wishlist> getById(String id);

    Wishlist save(Wishlist wishlist);

    void deleteById(String id);
}
