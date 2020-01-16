package com.future.medan.backend.services.impl;

import com.future.medan.backend.exceptions.ResourceNotFoundException;
import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.models.entity.User;
import com.future.medan.backend.models.entity.Wishlist;
import com.future.medan.backend.repositories.WishlistRepository;
import com.future.medan.backend.services.WishlistService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;
import java.util.*;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class WishlistImplTests {

    @Mock
    private WishlistRepository wishlistRepository;

    private WishlistService wishlistService;

    private Wishlist wishlist, wishlist2;
    private String findId, findId2, userId1, userId2;

    private User user1, user2;
    private Product product1, product2;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);

        this.wishlistService = new WishlistServiceImpl(wishlistRepository);

        this.findId = "ABCD";
        this.findId2 = "id-unavailable";
        this.userId1 = "syntialai";
        this.userId2 = "andrewwijaya";
        this.wishlist = Wishlist.builder().build();
        this.wishlist2 = Wishlist.builder().build();

        this.user1 = User.builder().build();
        user1.setId(userId1);

        this.product1 = Product.builder().build();
        product1.setId(findId);
        product1.setName("product1");
        product1.setHidden(false);

        this.product2 = Product.builder().build();
        product2.setId(findId2);
        product2.setName("product2");
        product2.setHidden(false);
    }

    @Test
    public void testGetAll(){
        List<Wishlist> expected = Arrays.asList(wishlist, wishlist2);

        when(wishlistRepository.findAll()).thenReturn(expected);
        List<Wishlist> actual = wishlistService.getAll();

        assertThat(actual, is(notNullValue()));
        assertThat(actual, hasItem(wishlist));
        assertThat(actual, hasItem(wishlist2));
    }

    @Test
    public void testGetById_OK(){
        when(wishlistRepository.findById(findId)).thenReturn(Optional.of(wishlist2));

        assertEquals(wishlistService.getById(findId), wishlist2);
    }

    @Test
    public void testGetByUserId_OK(){
        when(wishlistRepository.findByUserId(userId1)).thenReturn(Optional.of(wishlist));

        assertEquals(wishlistService.getByUserId(userId1), wishlist);
    }

    @Test
    public void testFindPaginated() {
        Pageable paging = PageRequest.of(1, 2);
        Page<Wishlist> expected = new PageImpl<>(Arrays.asList(wishlist, wishlist2));

        when(wishlistRepository.findAll(paging)).thenReturn(expected);

        assertEquals(expected, wishlistService.findPaginated(1, 2));
    }

    @Test
    public void testSave(){
        Set<Product> products = new HashSet<>();
        products.add(product1);

        wishlist.setId(userId1);
        wishlist.setProducts(products);
        when(wishlistRepository.save(any(Wishlist.class))).thenReturn(wishlist);

        assertEquals(wishlistService.save(user1, product1), wishlist);
    }

    @Test
    public void testSaveNull(){
        Set<Product> products = new HashSet<>();
        products.add(product1);

        wishlist.setId(userId1);
        wishlist.setProducts(products);
        when(wishlistRepository.findByUserId(userId1)).thenReturn(Optional.ofNullable(wishlist));
        when(wishlistRepository.save(any(Wishlist.class))).thenReturn(wishlist);

        assertEquals(wishlistService.save(user1, product1), wishlist);
    }

    @Test
    public void testEditById_OK(){
        when(wishlistRepository.existsById(findId)).thenReturn(Boolean.TRUE);
        when(wishlistRepository.save(wishlist)).thenReturn(wishlist);

        Wishlist actual = wishlistService.save(wishlist, findId);
        assertThat(actual, is(notNullValue()));
        assertEquals(actual, wishlist);
    }

    @Test
    public void testDeleteByProductId_OK(){
        Set<Product> products = new HashSet<>();
        Set<Product> filteredProducts = new HashSet<>();

        filteredProducts.add(product2);
        products.add(product1);
        products.add(product2);

        wishlist.setId(userId1);
        wishlist.setProducts(products);
        wishlist2.setId(userId1);
        wishlist2.setProducts(filteredProducts);

        when(wishlistRepository.findByUserId(userId1)).thenReturn(Optional.ofNullable(wishlist));
        when(wishlistRepository.save(wishlist2)).thenReturn(wishlist2);

        assertEquals(wishlistService.deleteByProductId(findId, userId1), wishlist2);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetById_NotFound(){
        when(wishlistRepository.findById(findId2)).thenReturn(Optional.empty());

        wishlistService.getById(findId2);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetByUserId_NotFound() {
            when(wishlistRepository.findByUserId(userId2)).thenReturn(Optional.empty());

            wishlistService.getByUserId(userId2);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testEditById_NotFound(){
        when(wishlistRepository.existsById(findId2))
                .thenThrow(new ResourceNotFoundException("Wishlist", "id", findId2));

        wishlistService.save(wishlist2, findId2);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testSave_NotFound() {
        when(wishlistRepository.existsById(userId1)).thenReturn(false);

        wishlistService.save(wishlist, userId1);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteById_NotFound(){
        when(wishlistRepository.findByUserId(userId2))
                .thenThrow(new ResourceNotFoundException("Wishlist", "user id", findId2));

        wishlistService.deleteByProductId(findId, userId1);
    }
}
