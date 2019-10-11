package com.future.medan.backend.services;

import com.future.medan.backend.models.entity.Purchase;

import java.util.List;

public interface PurchaseService {

    List<Purchase> getAll();

    Purchase getById(String id);

    Purchase save(Purchase purchase);

    void deleteById(String id);
}
