package com.future.medan.backend.services.impl;

import com.future.medan.backend.models.entity.Cart;
import com.future.medan.backend.repositories.CartRepository;
import com.future.medan.backend.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private CartRepository cartRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository){
        this.cartRepository = cartRepository;
    }

    @Override
    public List<Cart> getAll(){
        return cartRepository.findAll();
    }

    @Override
    public Optional<Cart> getById(String id){
        return cartRepository.findById(id);
    }

    @Override
    public Cart save(Cart cart){
        return cartRepository.save(cart);
    }

    @Override
    public void deleteById(String id){
        cartRepository.deleteById(id);
    }
}
