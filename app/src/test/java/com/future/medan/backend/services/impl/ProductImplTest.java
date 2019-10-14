package com.future.medan.backend.services.impl;

import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.repositories.ProductRepository;
import com.future.medan.backend.services.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

public class ProductImplTest {

    private ProductRepository productRepository;

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
    public void testGetAll() {
        // given
        List<Product> expectedProducts = Arrays.asList(product, product2);

        // when
        List<Product> actualProducts = productService.getAll();
        when(actualProducts).thenReturn(expectedProducts);

        // then
        assertEquals(actualProducts, expectedProducts);
    }

    @Test
    public void testGetById(){
        Product result = new Product();
        result.setName("my Product");

        Product actual = this.productService.getById(findId);
        Product actual2 = this.productService.getById(findId2);

        assertEquals(actual.getName(), result.getName());
        assertNull(actual2);
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
