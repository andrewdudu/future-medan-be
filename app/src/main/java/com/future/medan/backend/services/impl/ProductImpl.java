package com.future.medan.backend.services.impl;

import com.future.medan.backend.exceptions.ResourceNotFoundException;
import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.repositories.ProductRepository;
import com.future.medan.backend.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductImpl implements ProductService {

    private ProductRepository productRepository;

    @Autowired
    public ProductImpl(ProductRepository repository) {
        this.productRepository = repository;
    }

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public Product getById(String id){
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product save(Product product, String id){
        if (!productRepository.existsById(id))
            throw new ResourceNotFoundException("Product", "id", id);
        else
            return productRepository.save(product);
    }

    @Override
    public void deleteById(String id){
        if (!productRepository.existsById(id))
            throw new ResourceNotFoundException("Product", "id", id);
        else
            productRepository.deleteById(id);
    }
}
