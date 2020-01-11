package com.future.medan.backend.services.impl;

import com.future.medan.backend.exceptions.ResourceNotFoundException;
import com.future.medan.backend.models.entity.Cart;
import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.models.entity.User;
import com.future.medan.backend.repositories.CartRepository;
import com.future.medan.backend.services.CartService;
import com.future.medan.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CartServiceImpl implements CartService {

    private CartRepository cartRepository;

    private UserService userService;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository,
                           UserService userService){
        this.cartRepository = cartRepository;
        this.userService = userService;
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
    public Cart save(User user, Product product) {
        Cart cart = cartRepository.findByUserId(user.getId()).orElse(null);

        if (cart != null) {
            Set<Product> products = cart.getProducts();
            products.add(product);

            cart.setProducts(products);
        } else {
            Set<Product> products = new HashSet<>();
            products.add(product);

            cart = Cart.builder()
                    .products(products)
                    .user(user)
                    .build();
        }

        return cartRepository.save(cart);
    }

    @Override
    public Cart save(Cart cart, String id) {
        if (!cartRepository.existsById(id))
            throw new ResourceNotFoundException("Cart", "id", id);
        else
            return cartRepository.save(cart);
    }

    @Override
    public void deleteByProductId(String productId, String userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Set<Product> products = cart.getProducts();
        Set<Product> filteredProducts = new HashSet<>();

        products.forEach(product -> {
            if (!product.getId().equals(productId))
                filteredProducts.add(product);
        });

        cart.setProducts(filteredProducts);

        cartRepository.save(cart);
    }
}
