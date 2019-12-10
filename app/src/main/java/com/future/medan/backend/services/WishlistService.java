package com.future.medan.backend.services;

import com.future.medan.backend.models.entity.Wishlist;

import java.util.List;

public interface WishlistService {

    List<Wishlist> getAll();

    Wishlist getById(String id);

    Wishlist getByUserId(String user_id);

    Wishlist save(Wishlist wishlist);

    Wishlist save(Wishlist wishlist, String id);

    void deleteById(String id);
}
