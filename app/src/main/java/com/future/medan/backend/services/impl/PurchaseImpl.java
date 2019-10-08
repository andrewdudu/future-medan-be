package com.future.medan.backend.services.impl;

import com.future.medan.backend.models.entity.Purchase;
import com.future.medan.backend.repositories.PurchaseRepository;
import com.future.medan.backend.services.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PurchaseImpl implements PurchaseService {

    private PurchaseRepository purchaseRepository;

    @Autowired
    public PurchaseImpl (PurchaseRepository purchaseRepository){
        this.purchaseRepository = purchaseRepository;
    }

    @Override
    public List<Purchase> getAll(){
        return purchaseRepository.findAll();
    }

    @Override
    public Optional<Purchase> getById(String id){
        return purchaseRepository.findById(id);
    }

    @Override
    public Purchase save(Purchase purchase){
        return purchaseRepository.save(purchase);
    }

    @Override
    public void deleteById(String id){
        purchaseRepository.deleteById(id);
    }
}
