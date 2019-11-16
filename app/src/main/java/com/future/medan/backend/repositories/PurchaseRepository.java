package com.future.medan.backend.repositories;

import com.future.medan.backend.models.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, String> {

    Purchase getByProductIdAndUserId(String productId, String userId);
}
