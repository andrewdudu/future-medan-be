package com.future.medan.backend.services.impl;

import com.future.medan.backend.models.entity.Wishlist;
import com.future.medan.backend.repositories.WishlistRepository;
import com.future.medan.backend.services.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishlistImpl implements WishlistService {

    private WishlistRepository wishlistRepository;

    @Autowired
    public WishlistImpl(WishlistRepository wishlistRepository){
        this.wishlistRepository = wishlistRepository;
    }

    @Override
    public List<Wishlist> getAll(){
        return wishlistRepository.findAll();
    }

    @Override
    public Optional<Wishlist> getById(String id){
        return wishlistRepository.findById(id);
    }

    @Override
    public Wishlist save(Wishlist wishlist){
        return wishlistRepository.save(wishlist);
    }

    @Override
    public void deleteById(String id){
        wishlistRepository.deleteById(id);
    }
}
