package com.future.medan.backend.services.impl;

import com.future.medan.backend.exceptions.ResourceNotFoundException;
import com.future.medan.backend.models.entity.Cart;
import com.future.medan.backend.repositories.CartRepository;
import com.future.medan.backend.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Cart getById(String id){
        return cartRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cart", "id", id));
    }

    @Override
    public Cart getByUserId(String user_id){
        return cartRepository.findByUserId(user_id).orElseThrow(() -> new ResourceNotFoundException("Cart", "user id", user_id));
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
