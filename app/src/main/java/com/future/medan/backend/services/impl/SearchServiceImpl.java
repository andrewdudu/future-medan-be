package com.future.medan.backend.services.impl;

import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.repositories.ProductRepository;
import com.future.medan.backend.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    private ProductRepository productRepository;

    @Autowired
    public SearchServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> search(String term) {
        return productRepository.searchProduct(term.toLowerCase());
    }
}
