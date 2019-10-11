package com.future.medan.backend.services.impl;

import com.future.medan.backend.exceptions.ResourceNotFoundException;
import com.future.medan.backend.models.entity.Cart;
import com.future.medan.backend.repositories.CartRepository;
import com.future.medan.backend.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartImpl implements CartService {

    private CartRepository cartRepository;

    @Autowired
    public CartImpl (CartRepository cartRepository){
        this.cartRepository = cartRepository;
    }

    @Override
    public List<Cart> getAll(){
        return cartRepository.findAll();
    }

    @Override
    public Cart getById(String id){
        return cartRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cart", "id", id));
    }

    @Override
    public Cart save(Cart cart){
        return cartRepository.save(cart);
    }

    @Override
    public Cart save(Cart cart, String id){
        if (!cartRepository.existsById(id))
            throw new ResourceNotFoundException("Cart", "id", id);
        else
            return cartRepository.save(cart);
    }

    @Override
    public void deleteById(String id){
        if (!cartRepository.existsById(id))
            throw new ResourceNotFoundException("Cart", "id", id);
        else
            cartRepository.deleteById(id);
    }
}
