package com.future.medan.backend.services.impl;

import com.future.medan.backend.exceptions.ResourceNotFoundException;
import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.repositories.ProductRepository;
import com.future.medan.backend.services.ProductService;
import com.future.medan.backend.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    private StorageService storageService;

    @Autowired
    public ProductServiceImpl(ProductRepository repository, StorageService storageService) {
        this.storageService = storageService;
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
    public Product save(Product product) throws IOException {
        String imagePath = storageService.storePdf(product.getImage(), product.getSku());

        product.setImage(imagePath);

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
