package com.future.medan.backend.services.impl;

import com.future.medan.backend.exceptions.ResourceNotFoundException;
import com.future.medan.backend.models.entity.Purchase;
import com.future.medan.backend.repositories.PurchaseRepository;
import com.future.medan.backend.services.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    private PurchaseRepository purchaseRepository;

    @Autowired
    public PurchaseServiceImpl(PurchaseRepository purchaseRepository){
        this.purchaseRepository = purchaseRepository;
    }

    @Override
    public Purchase getByProductIdAndUserId(String productId, String userId) {
        return purchaseRepository.getByProductIdAndUserId(productId, userId);
    }

    @Override
    public Set<Purchase> getByOrderId(String orderId) {
        return purchaseRepository.getAllByOrderId(orderId);
    }

    @Override
    public Set<Purchase> getAllByUserId(String userId) {
        return purchaseRepository.getAllByUserIdAndStatusIsNot(userId, "CANCEL");
    }

    @Override
    public List<Purchase> getAll(){
        return purchaseRepository.findAll();
    }

    @Override
    public Purchase getById(String id){
        return purchaseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Purchase", "id", id));
    }

    @Override
    public Purchase save(Purchase purchase){
        return purchaseRepository.save(purchase);
    }

    @Override
    public Purchase save(Purchase purchase, String id){
        if (!purchaseRepository.existsById(id))
            throw new ResourceNotFoundException("Purchase", "id", id);
        else
            return purchaseRepository.save(purchase);
    }

    @Override
    public void deleteById(String id){
        if (!purchaseRepository.existsById(id))
            throw new ResourceNotFoundException("Purchase", "id", id);
        else
            purchaseRepository.deleteById(id);
    }
}
