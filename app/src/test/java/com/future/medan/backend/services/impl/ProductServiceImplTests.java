package com.future.medan.backend.services.impl;

import com.future.medan.backend.exceptions.ResourceNotFoundException;
import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.repositories.ProductRepository;
import com.future.medan.backend.services.ProductService;
import com.future.medan.backend.services.SequenceService;
import com.future.medan.backend.services.StorageService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

public class ProductServiceImplTests {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductService productService;

    @Mock
    private StorageService storageService;

    @Mock
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
                .image("/api/get-img/imagename.png")
                .author("string")
                .hidden(false)
                .build();
        this.product2 = Product.builder()
                .name("my Product")
                .sku("string")
                .description("string")
                .price(new BigDecimal("0"))
                .image("/api/get-img/imagename.png")
                .author("string")
                .hidden(false)
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
    public void testGetAllWithoutHidden() {
        List<Product> products = Arrays.asList(product, product2);
        when(productRepository.getAllByHiddenIs(false)).thenReturn(products);
        List<Product> actual = productService.getAllWithoutHidden();

        assertEquals(products, actual);
    }

    @Test
    public void testGetByCategoryIdWithoutHidden() {
        List<Product> products = Arrays.asList(product, product2);
        when(productRepository.getByCategoryIdAndHiddenIs(findId, false)).thenReturn(products);

        assertEquals(products, productService.getByCategoryIdWithoutHidden(findId));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetByCategoryIdWithoutHidden_NotFound() {
        when(productRepository.getByCategoryIdAndHiddenIs(findId, false)).thenThrow(new ResourceNotFoundException("Product", "Category", findId));

        productService.getByCategoryIdWithoutHidden(findId);
    }

    @Test
    public void testGetByMerchantId() {
        List<Product> products = Arrays.asList(product, product2);
        when(productRepository.getByMerchantId(findId)).thenReturn(products);

        assertEquals(products, productService.getByMerchantId(findId));
    }

    @Test
    public void testFindPaginated() {
        Pageable paging = PageRequest.of(1, 2);
        Page<Product> expected = new PageImpl<>(Arrays.asList(product, product2));

        when(productRepository.findAll(paging)).thenReturn(expected);

        assertEquals(expected, productService.findPaginated(1, 2));
    }

    @Test
    public void testFindByIdIn() {
        Set<Product> products = new HashSet<>(Arrays.asList(product, product2));
        Set<String> ids = new HashSet<>(Arrays.asList(findId, findId2));
        when(productRepository.findByIdIn(ids)).thenReturn(products);

        assertEquals(products, productService.findByIdIn(ids));
    }

    @Test
    public void testHide() {
        when(productRepository.findById(findId)).thenReturn(Optional.ofNullable(product));
        when(productRepository.save(product)).thenReturn(product);

        assertEquals(product, productService.hide(findId));
    }

    @Test
    public void testGetById_OK(){
        when(productRepository.findById(findId)).thenReturn(Optional.of(product2));

        assertEquals(productService.getById(findId), product2);
    }

    @Test
    public void testSave() throws IOException {
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(sequenceService.save(any(String.class))).thenReturn("sku");
        when(storageService.storeImage(any(String.class), any(String.class))).thenReturn("path");
        when(storageService.storePdf(any(String.class), any(String.class))).thenReturn("path");

        assertEquals(product, productService.save(product));
    }

    @Test
    public void testEditById_OK() throws IOException {
        when(productRepository.findById(findId)).thenReturn(Optional.ofNullable(product));
        when(storageService.storePdf(any(String.class), any(String.class))).thenReturn(product.getPdf());
        when(storageService.storeImage(any(String.class), any(String.class))).thenReturn(product.getImage());

        when(productRepository.save(product)).thenReturn(product);

        assertEquals(product, productService.save(product, findId));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testEditById_NotFound() throws IOException {
        when(productRepository.findById(findId)).thenReturn(Optional.empty());

        productService.save(product, findId);
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
    public void testDeleteById_NotFound(){
        when(productRepository.existsById(findId2)).thenReturn(false);

        productService.deleteById(findId2);
    }
}
