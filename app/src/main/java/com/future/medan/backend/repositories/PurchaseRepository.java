package com.future.medan.backend.repositories;

import com.future.medan.backend.models.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, String> {

    Purchase getByProductIdAndUserIdAndStatusIs(String productId, String userId, String status);

    Purchase findOneByOrderIdAndProductId(String orderId, String productId);

    List<Purchase> getByMerchantIdAndStatus(String merchantId, String status);

    List<Purchase> getByOrderIdAndProductId(String orderId, String productId);

    Set<Purchase> getAllByOrderId(String orderId);

    Set<Purchase> getAllByUserIdAndStatusIsNot(String userId, String status);

    Boolean existsByUserIdAndProductId(String userId, String productId);
}
