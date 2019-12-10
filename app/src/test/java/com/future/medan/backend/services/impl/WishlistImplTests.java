package com.future.medan.backend.services.impl;

import com.future.medan.backend.exceptions.ResourceNotFoundException;
import com.future.medan.backend.models.entity.Wishlist;
import com.future.medan.backend.repositories.WishlistRepository;
import com.future.medan.backend.services.WishlistService;
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

public class WishlistImplTests {

    @Mock
    private WishlistRepository wishlistRepository;

    private WishlistService wishlistService;

    private Wishlist wishlist, wishlist2;
    private String findId, findId2, userId1, userId2;

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
    public void testSave(){
        when(wishlistRepository.save(any(Wishlist.class))).thenReturn(wishlist);

        assertThat(wishlistService.save(wishlist), is(notNullValue()));
    }

    @Test
    public void testEditById_OK(){
        when(wishlistRepository.existsById(findId)).thenReturn(Boolean.TRUE);
        when(wishlistRepository.save(wishlist)).thenReturn(wishlist);

        Wishlist actual = wishlistService.save(wishlist, findId);
        assertThat(actual, is(notNullValue()));
        assertEquals(actual, wishlist);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteById_OK(){
        when(wishlistRepository.existsById(findId)).thenReturn(Boolean.TRUE);
        when(wishlistRepository.findById(findId))
                .thenThrow(new ResourceNotFoundException("Wishlist", "id", findId2));

        wishlistService.deleteById(findId);
        wishlistService.getById(findId2);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetById_NotFound(){
        when(wishlistRepository.findById(findId2))
                .thenThrow(new ResourceNotFoundException("Wishlist", "id", findId2));

        wishlistService.getById(findId2);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetByUserId_NotFound(){
        when(wishlistRepository.findByUserId(userId2))
            .thenThrow(new ResourceNotFoundException("Wishlist", "user id", userId2));

        wishlistService.getByUserId(userId2);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testEditById_NotFound(){
        when(wishlistRepository.existsById(findId2))
                .thenThrow(new ResourceNotFoundException("Wishlist", "id", findId2));

        wishlistService.save(wishlist2, findId2);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteById_NotFound(){
        when(wishlistRepository.existsById(findId2))
                .thenThrow(new ResourceNotFoundException("Wishlist", "id", findId2));

        wishlistService.deleteById(findId2);
    }
}
