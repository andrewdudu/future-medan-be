package com.future.medan.backend.repositories;

import com.future.medan.backend.models.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, String> {

    Purchase getByProductIdAndUserId(String productId, String userId);

    Set<Purchase> getAllByOrderId(String orderId);

    Set<Purchase> getAllByUserIdAndStatusIsNot(String userId, String status);
}
