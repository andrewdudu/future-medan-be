package com.future.medan.backend.services;

import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.models.entity.User;
import com.future.medan.backend.models.entity.Wishlist;
import org.springframework.data.domain.Page;

import java.util.List;

public interface WishlistService {

    List<Wishlist> getAll();

    Wishlist getById(String id);

    Wishlist getByUserId(String user_id);

    Page<Wishlist> findPaginated(int page, int size);

    Wishlist save(User user, Product product);

    Wishlist save(Wishlist wishlist, String id);

    Wishlist deleteByProductId(String product_id, String user_id);
}
