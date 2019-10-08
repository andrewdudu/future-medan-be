package com.future.medan.backend.services;

import com.future.medan.backend.models.entity.Purchase;

import java.util.List;
import java.util.Optional;

public interface PurchaseService {

    List<Purchase> getAll();

    Optional<Purchase> getById(String id);

    Purchase save(Purchase purchase);

    void deleteById(String id);
}
