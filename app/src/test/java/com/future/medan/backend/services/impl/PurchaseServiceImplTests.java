package com.future.medan.backend.services.impl;

import com.future.medan.backend.exceptions.ResourceNotFoundException;
import com.future.medan.backend.models.entity.Purchase;
import com.future.medan.backend.repositories.PurchaseRepository;
import com.future.medan.backend.services.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PurchaseServiceImplTests {

    @Mock
    private PurchaseRepository purchaseRepository;

    private PurchaseService purchaseService;

    private ProductService productService;

    private UserService userService;

    private CartService cartService;

    private SequenceService sequenceService;

    private Purchase purchase, purchase2;
    private String findId, findId2;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);

        this.purchaseService = new PurchaseServiceImpl(purchaseRepository, productService,  userService, cartService, sequenceService);

        this.findId = "ABCD";
        this.findId2 = "id-unavailable";
        this.purchase = Purchase.builder()
                .build();
        this.purchase2 = Purchase.builder()
                .build();
    }

    @Test
    public void testGetAll(){
        List<Purchase> expected = Arrays.asList(purchase, purchase2);

        when(purchaseRepository.findAll()).thenReturn(expected);
        List<Purchase> actual = purchaseService.getAll();

        assertThat(actual, is(notNullValue()));
        assertThat(actual, hasItem(purchase));
        assertThat(actual, hasItem(purchase2));
    }

    @Test
    public void testGetById_OK(){
        when(purchaseRepository.findById(findId)).thenReturn(Optional.of(purchase2));

        assertEquals(purchaseService.getById(findId), purchase2);
    }

    @Test
    public void testSave(){
        when(purchaseRepository.save(any(Purchase.class))).thenReturn(purchase);

        assertThat(purchaseService.save(purchase), is(notNullValue()));
    }

    @Test
    public void testEditById_OK(){
        when(purchaseRepository.existsById(findId)).thenReturn(Boolean.TRUE);
        when(purchaseRepository.save(purchase)).thenReturn(purchase);

        Purchase actual = purchaseService.save(purchase, findId);
        assertThat(actual, is(notNullValue()));
        assertEquals(actual, purchase);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteById_OK(){
        when(purchaseRepository.existsById(findId)).thenReturn(Boolean.TRUE);
        when(purchaseRepository.findById(findId))
                .thenThrow(new ResourceNotFoundException("Purchase", "id", findId2));

        purchaseService.deleteById(findId);
        purchaseService.getById(findId2);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetById_NotFound(){
        when(purchaseRepository.findById(findId2))
                .thenThrow(new ResourceNotFoundException("Purchase", "id", findId2));

        purchaseService.getById(findId2);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testEditById_NotFound(){
        when(purchaseRepository.existsById(findId2))
                .thenThrow(new ResourceNotFoundException("Purchase", "id", findId2));

        purchaseService.save(purchase2, findId2);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteById_NotFound(){
        when(purchaseRepository.existsById(findId2))
                .thenThrow(new ResourceNotFoundException("Purchase", "id", findId2));

        purchaseService.deleteById(findId2);
    }
}
