package com.future.medan.backend.services.impl;

import com.future.medan.backend.exceptions.ResourceNotFoundException;
import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.repositories.ProductRepository;
import com.future.medan.backend.services.SearchService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class SearchServiceImplTests {

    @Mock
    private ProductRepository productRepository;

    private SearchService searchService;

    private Product product1, product2;

    private String termSuccess, termNotFound;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        this.searchService = new SearchServiceImpl(productRepository);

        this.termSuccess = "Buku sukses";
        this.termNotFound = "Buku belum ada";

        this.product1 = Product.builder()
                .name("Product Test")
                .author("Author 1")
                .image("/api/get-img/1cab35be-cb0f-4db9-87f9-7b47db38f7ac.png")
                .sku("sku-1")
                .description("one")
                .price(new BigDecimal(50000))
                .hidden(false)
                .build();
        this.product2 = Product.builder()
                .name("Product Test 2")
                .author("Author 2")
                .image("/api/get-img/1cab35be-cb0f-4db9-87f9-7b47db38f7ac.png")
                .sku("sku-2")
                .description("two")
                .price(new BigDecimal(10000))
                .hidden(false)
                .build();
    }

    @Test
    public void testSearch_Ok() {
        List<Product> expected = Arrays.asList(product1, product2);
        when(productRepository.searchProduct(termSuccess.toLowerCase())).thenReturn(expected);

        List<Product> actual = searchService.search(termSuccess.toLowerCase());

        assertEquals(expected, actual);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testSearch_NotFound() {
        when(productRepository.searchProduct(termNotFound.toLowerCase()))
                .thenThrow(new ResourceNotFoundException("Search", "term", termNotFound));

        searchService.search(termNotFound.toLowerCase());
    }
}
