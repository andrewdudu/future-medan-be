package com.future.medan.backend.services.impl;

import com.future.medan.backend.exceptions.ResourceNotFoundException;
import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.repositories.ProductRepository;
import com.future.medan.backend.services.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ProductServiceImplTests {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductService productService;

    private StorageService storageService;

    private SequenceService sequenceService;

    private Product product, product2;
    private String findId, findId2;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);

        this.productService = new ProductServiceImpl(productRepository, storageService, sequenceService);

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

    @Test
    public void testGetAll(){
        List<Product> expected = Arrays.asList(product, product2);

        when(productRepository.findAll()).thenReturn(expected);
        List<Product> actual = productService.getAll();

        assertThat(actual, is(notNullValue()));
        assertThat(actual, hasItem(product));
        assertThat(actual, hasItem(product2));
    }

    @Test
    public void testGetById_OK(){
        when(productRepository.findById(findId)).thenReturn(Optional.of(product2));

        assertEquals(productService.getById(findId), product2);
    }

    @Test
    public void testSave() throws IOException {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        assertThat(productService.save(product), is(notNullValue()));
    }

    @Test
    public void testEditById_OK() throws IOException {
        when(productRepository.existsById(findId)).thenReturn(Boolean.TRUE);
        when(productRepository.save(product)).thenReturn(product);

        Product actual = productService.save(product, findId);
        assertThat(actual, is(notNullValue()));
        assertEquals(actual, product);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteById_OK(){
        when(productRepository.existsById(findId)).thenReturn(Boolean.TRUE);
        when(productRepository.findById(findId))
                .thenThrow(new ResourceNotFoundException("Product", "id", findId2));

        productService.deleteById(findId);
        productService.getById(findId2);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetById_NotFound(){
        when(productRepository.findById(findId2))
                .thenThrow(new ResourceNotFoundException("Product", "id", findId2));

        productService.getById(findId2);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testEditById_NotFound() throws IOException {
        when(productRepository.existsById(findId2))
                .thenThrow(new ResourceNotFoundException("Product", "id", findId2));

        productService.save(product2, findId2);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteById_NotFound(){
        when(productRepository.existsById(findId2))
                .thenThrow(new ResourceNotFoundException("Product", "id", findId2));

        productService.deleteById(findId2);
    }
}
