package com.future.medan.backend.services.impl;

import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.repositories.ProductRepository;
import com.future.medan.backend.services.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ProductImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product, product2;

    private String findId, findId2;

    @Before
    public void setup(){
        this.findId = "7892b1a2-0953-4071-9ffe-a5e193255585";
        this.findId2 = "id-unavailable";
        this.product = Product.builder()
                .name("string")
                .sku("string")
                .description("string")
                .price(new BigDecimal("0"))
                .image("string")
                .author("string")
                .build();
        this.product2 = Product.builder()
                .name("my Product")
                .sku("string")
                .description("string")
                .price(new BigDecimal("0"))
                .image("img")
                .author("string")
                .build();
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSave(){

    }

    @Test
    public void testEditById(){

    }

    @Test
    public void testDeleteById(){
        productService.deleteById(findId);
        productService.deleteById(findId2);

        assertNull(this.productService.getById(findId));
        assertNull(this.productService.getById(findId2));
    }
}
