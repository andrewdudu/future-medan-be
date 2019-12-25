package com.future.medan.backend.services.impl;

import com.future.medan.backend.exceptions.ResourceNotFoundException;
import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.models.entity.User;
import com.future.medan.backend.models.entity.Wishlist;
import com.future.medan.backend.repositories.WishlistRepository;
import com.future.medan.backend.services.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public Wishlist getByUserId(String user_id) {
        return wishlistRepository
                .findByUserId(user_id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Wishlist", "user id", user_id));
    }

    @Override
    public Wishlist save(User user, Product product) {
        Wishlist wishlist = wishlistRepository.findByUserId(user.getId()).orElse(null);

        if (wishlist != null) {
            Set<Product> products = wishlist.getProducts();
            products.add(product);

            wishlist.setProducts(products);
        }
        else {
            Set<Product> products = new HashSet<>();
            products.add(product);

            wishlist = Wishlist.builder()
                    .products(products)
                    .user(user)
                    .build();
        }

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
    public Wishlist deleteByProductId(String productId, String userId) {
        Wishlist wishlist = wishlistRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Set<Product> products = wishlist.getProducts();
        Set<Product> filteredProducts = new HashSet<>();

        products.forEach(product -> {
            if (!product.getId().equals(productId)) {
                filteredProducts.add(product);
            }
        });

        wishlist.setProducts(filteredProducts);
        return wishlistRepository.save(wishlist);
    }
}
