package com.future.medan.backend.services.impl;

import com.future.medan.backend.exceptions.ResourceNotFoundException;
import com.future.medan.backend.models.entity.Cart;
import com.future.medan.backend.repositories.CartRepository;
import com.future.medan.backend.services.CartService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CartImplTests {

    @Mock
    private CartRepository cartRepository;

    private CartService cartService;

    private Cart cart, cart2;
    private String findId, findId2, userId;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);

        this.cartService = new CartImpl(cartRepository);

        this.findId = "ABCD";
        this.findId2 = "id-unavailable";
        this.userId = "ini-user-id-1";
        this.cart = Cart.builder().build();
        this.cart2 = Cart.builder().build();
    }

    @Test
    public void testGetAll(){
        List<Cart> expected = Arrays.asList(cart, cart2);

        when(cartRepository.findAll()).thenReturn(expected);
        List<Cart> actual = cartService.getAll();

        assertThat(actual, is(notNullValue()));
        assertThat(actual, hasItem(cart));
        assertThat(actual, hasItem(cart2));
    }

    @Test
    public void testGetById_OK(){
        when(cartRepository.findById(findId)).thenReturn(Optional.of(cart2));

        assertEquals(cartService.getById(findId), cart2);
    }

    @Test
    public void testGetByUserId_OK(){
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));

        assertEquals(cartService.getByUserId(userId), cart);
    }

    @Test
    public void testSave(){
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        assertThat(cartService.save(cart), is(notNullValue()));
    }

    @Test
    public void testEditById_OK(){
        when(cartRepository.existsById(findId)).thenReturn(Boolean.TRUE);
        when(cartRepository.save(cart)).thenReturn(cart);

        Cart actual = cartService.save(cart, findId);
        assertThat(actual, is(notNullValue()));
        assertEquals(actual, cart);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteById_OK(){
        when(cartRepository.existsById(findId)).thenReturn(Boolean.TRUE);
        when(cartRepository.findById(findId))
                .thenThrow(new ResourceNotFoundException("Cart", "id", findId2));

        cartService.deleteById(findId);
        cartService.getById(findId2);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetById_NotFound(){
        when(cartRepository.findById(findId2))
                .thenThrow(new ResourceNotFoundException("Cart", "id", findId2));

        cartService.getById(findId2);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetByUserId_NotFound(){
        when(cartRepository.findByUserId(userId))
                .thenThrow(new ResourceNotFoundException("Cart", "user id", userId));

        cartService.getByUserId(userId);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testEditById_NotFound(){
        when(cartRepository.existsById(findId2))
                .thenThrow(new ResourceNotFoundException("Cart", "id", findId2));

        cartService.save(cart2, findId2);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteById_NotFound(){
        when(cartRepository.existsById(findId2))
                .thenThrow(new ResourceNotFoundException("Cart", "id", findId2));

        cartService.deleteById(findId2);
    }
}
