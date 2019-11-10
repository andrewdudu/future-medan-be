package com.future.medan.backend.services.impl;

import com.future.medan.backend.exceptions.ResourceNotFoundException;
import com.future.medan.backend.models.entity.Wishlist;
import com.future.medan.backend.repositories.WishlistRepository;
import com.future.medan.backend.services.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistServiceImpl implements WishlistService {

    private WishlistRepository wishlistRepository;

    @Autowired
    public WishlistServiceImpl(WishlistRepository wishlistRepository){
        this.wishlistRepository = wishlistRepository;
    }

    @Override
    public List<Wishlist> getAll(){
        return wishlistRepository.findAll();
    }

    @Override
    public Wishlist getById(String id){
        return wishlistRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Wishlist", "id", id));
    }

    @Override
    public Wishlist save(Wishlist wishlist){
        return wishlistRepository.save(wishlist);
    }

    @Override
    public Wishlist save(Wishlist wishlist, String id){
        if (!wishlistRepository.existsById(id))
            throw new ResourceNotFoundException("Wishlist", "id", id);
        else
            return wishlistRepository.save(wishlist);
    }

    @Override
    public void deleteById(String id){
        if (!wishlistRepository.existsById(id))
            throw new ResourceNotFoundException("Wishlist", "id", id);
        else
            wishlistRepository.deleteById(id);
    }
}
