package com.future.medan.backend.repositories;

import com.future.medan.backend.models.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, String> {

    Optional<Wishlist> findByUserId(String user_id);
}
