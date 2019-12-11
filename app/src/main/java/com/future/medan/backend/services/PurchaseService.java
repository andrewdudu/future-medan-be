package com.future.medan.backend.services;

import com.future.medan.backend.models.entity.Purchase;

import java.util.List;
import java.util.Set;

public interface PurchaseService {

    List<Purchase> getAll();

    Purchase getById(String id);

    Purchase getByProductIdAndUserId(String productId, String userId);

    Set<Purchase> getByOrderId(String orderId);

    Set<Purchase> getAllByUserId(String userId);

    Purchase save(Purchase purchase);

    Purchase save(Purchase purchase, String id);

    void deleteById(String id);
}
