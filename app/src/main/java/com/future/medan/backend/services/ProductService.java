package com.future.medan.backend.services;

import com.future.medan.backend.models.entity.Product;

import java.util.List;

public interface ProductService {

    List<Product> getAll();

    Product save(Product product);

    Product getOne(String id);

    void deleteById(String id);
}
