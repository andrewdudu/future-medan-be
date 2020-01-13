package com.future.medan.backend.services;

import com.future.medan.backend.models.entity.Product;

import java.util.List;

public interface SearchService {

    List<Product> search(String term);
}
