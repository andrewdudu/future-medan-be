package com.future.medan.backend.services.impl;

import com.future.medan.backend.exceptions.BadRequestException;
import com.future.medan.backend.exceptions.ResourceNotFoundException;
import com.future.medan.backend.models.entity.Cart;
import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.models.entity.Purchase;
import com.future.medan.backend.models.entity.User;
import com.future.medan.backend.payload.requests.PurchaseWebRequest;
import com.future.medan.backend.repositories.PurchaseRepository;
import com.future.medan.backend.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    private PurchaseRepository purchaseRepository;

    private ProductService productService;

    private UserService userService;

    private CartService cartService;

    private SequenceService sequenceService;

    @Autowired
    public PurchaseServiceImpl(PurchaseRepository purchaseRepository,
                               ProductService productService,
                               UserService userService,
                               CartService cartService,
                               SequenceService sequenceService){
        this.purchaseRepository = purchaseRepository;
        this.productService = productService;
        this.userService = userService;
        this.cartService = cartService;
        this.sequenceService = sequenceService;
    }

    @Override
    public Set<Purchase> getPurchasedProduct(String userId) {
        return purchaseRepository.getAllByUserIdAndStatusIsNot(userId, "CANCEL");
    }

    @Override
    public Purchase getByProductIdAndUserId(String productId, String userId) {
        return purchaseRepository.getByProductIdAndUserIdAndStatusIs(productId, userId, "APPROVED");
    }

    @Override
    public Set<Purchase> getByOrderId(String orderId) {
        return purchaseRepository.getAllByOrderId(orderId);
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
    public void save(PurchaseWebRequest purchaseWebRequest, String userId){
        Set<Product> products = productService.findByIdIn(purchaseWebRequest.getProducts());
        User user = userService.getById(userId);
        String orderId = sequenceService.save(userId.substring(0, 3).toUpperCase());
        Cart cart = cartService.getByUserId(userId);
        Set<Product> filteredProducts = cart.getProducts();
        Set<Product> tempProducts = new HashSet<>();

        filteredProducts.forEach(filteredProduct -> {
            products.forEach(product -> {
                if (filteredProduct.equals(product)) {
                    tempProducts.add(product);
                }
            });
        });

        if (tempProducts.size() != products.size()) throw new ResourceNotFoundException("Product", "id", purchaseWebRequest.getProducts());

        products.forEach(product -> {
            if (purchaseRepository.existsByUserIdAndProductId(userId, product.getId())) throw new BadRequestException("You have purchased the product");

            Purchase purchase = new Purchase();
            purchase.setUser(user);
            purchase.setOrderId(orderId);
            purchase.setProduct(product);
            purchase.setStatus("PENDING");
            purchase.setMerchant(product.getMerchant());

            purchaseRepository.save(purchase);

            filteredProducts.remove(product);
        });

        cart.setProducts(filteredProducts);

        cartService.save(cart, cart.getId());
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
