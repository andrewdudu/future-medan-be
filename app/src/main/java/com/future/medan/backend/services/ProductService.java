package com.future.medan.backend.services;

import com.future.medan.backend.models.entity.Product;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    List<Product> getAll();

    Product getById(String id);

    Product hide(String id);

    Product save(Product product) throws IOException;

    Product save(Product product, String id);

    void deleteById(String id);
}
