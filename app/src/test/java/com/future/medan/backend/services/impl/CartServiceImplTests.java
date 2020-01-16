package com.future.medan.backend.services.impl;

import com.future.medan.backend.exceptions.ResourceNotFoundException;
import com.future.medan.backend.models.entity.Cart;
import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.models.entity.Role;
import com.future.medan.backend.models.entity.User;
import com.future.medan.backend.models.enums.RoleEnum;
import com.future.medan.backend.repositories.CartRepository;
import com.future.medan.backend.services.CartService;
import com.future.medan.backend.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.*;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class CartServiceImplTests {

    @Mock
    private CartRepository cartRepository;

    private UserService userService;

    private CartService cartService;

    private Cart cart, cart2;

    private Product product, product2;

    private User user, user2;

    private String findId, findId2, userId, productId;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);

        this.cartService = new CartServiceImpl(cartRepository, userService);

        this.findId = "ABCD";
        this.findId2 = "id-unavailable";
        this.userId = "ini-user-id-1";
        this.productId = "product-id";

        this.product = Product.builder()
                .name("string")
                .sku("string")
                .description("string")
                .price(new BigDecimal("0"))
                .image("/api/get-img/imagename.png")
                .author("string")
                .hidden(false)
                .build();

        this.product.setId("test-id");

        this.product2 = Product.builder()
                .name("my Product")
                .sku("string")
                .description("string")
                .price(new BigDecimal("0"))
                .image("/api/get-img/imagename.png")
                .author("string")
                .hidden(false)
                .build();

        this.user = User.builder()
                .description("Test Description")
                .email("test@example.com")
                .image("/api/get-img/1cab35be-cb0f-4db9-87f9-7b47db38f7ac.png")
                .name("Test Book")
                .password("Test")
                .roles(new HashSet(Collections.singleton(new Role(RoleEnum.ROLE_USER))))
                .status(true)
                .username("testusername")
                .build();
        this.user2 = User.builder()
                .description("Test Description")
                .email("test@example.com")
                .image("/api/get-img/1cab35be-cb0f-4db9-87f9-7b47db38f7ac.png")
                .name("Test Book")
                .password("Test")
                .roles(new HashSet(Collections.singleton(new Role(RoleEnum.ROLE_USER))))
                .status(true)
                .username("testusername")
                .build();

        this.cart = Cart.builder()
                .products(new HashSet<>(Collections.singletonList(product)))
                .user(user)
                .build();
        this.cart2 = Cart.builder()
                .products(new HashSet<>(Collections.singletonList(product)))
                .user(user)
                .build();
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
    public void testGetById_Ok(){
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
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.ofNullable(cart));
        when(cartRepository.save(cart)).thenReturn(cart);

        Set<Product> products = new HashSet<>(Collections.singletonList(product));

        cart.setProducts(products);
        user.setId(userId);

        assertEquals(cart, cartService.save(user, product));
    }

    @Test
    public void testSaveNull(){
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(cartRepository.save(cart)).thenReturn(cart);

        Set<Product> products = new HashSet<>(Collections.singletonList(product));

        cart.setProducts(products);

        assertEquals(cart, cartService.save(user, product));
    }

    @Test
    public void testEditById_OK(){
        when(cartRepository.existsById(findId)).thenReturn(Boolean.TRUE);
        when(cartRepository.save(cart)).thenReturn(cart);

        Cart actual = cartService.save(cart, findId);
        assertThat(actual, is(notNullValue()));
        assertEquals(actual, cart);
    }

    @Test
    public void testDeleteById_OK(){
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.ofNullable(cart));
        when(cartRepository.save(cart)).thenReturn(cart);

        cartService.deleteByProductId(productId, userId);
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
        when(cartRepository.existsById(findId2)).thenReturn(false);

        cartService.save(cart2, findId2);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteById_NotFound(){
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());

        cartService.deleteByProductId(productId, userId);
    }
}
