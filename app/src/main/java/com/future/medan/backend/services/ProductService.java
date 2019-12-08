package com.future.medan.backend.services;

import com.future.medan.backend.models.entity.Product;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface ProductService {

    List<Product> getAll();

    Set<Product> findByIdIn(Set<String> id);

    Product getById(String id);

    Product hide(String id);

    Product save(Product product) throws IOException;

    Product save(Product product, String id);

    void deleteById(String id);
}
