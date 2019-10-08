package com.future.medan.backend.services;

import com.future.medan.backend.models.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> getAll();

    Optional<Product> getById(String id);

    Product save(Product product);

    void deleteById(String id);
}
