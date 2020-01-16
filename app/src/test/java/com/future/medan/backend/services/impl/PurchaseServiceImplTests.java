package com.future.medan.backend.services.impl;

import com.future.medan.backend.exceptions.BadRequestException;
import com.future.medan.backend.exceptions.ResourceNotFoundException;
import com.future.medan.backend.models.entity.*;
import com.future.medan.backend.models.enums.RoleEnum;
import com.future.medan.backend.payload.requests.PurchaseWebRequest;
import com.future.medan.backend.repositories.PurchaseRepository;
import com.future.medan.backend.services.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.*;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PurchaseServiceImplTests {

    @Mock
    private PurchaseRepository purchaseRepository;

    private PurchaseService purchaseService;

    @Mock
    private ProductService productService;

    @Mock
    private UserService userService;

    @Mock
    private CartService cartService;

    @Mock
    private SequenceService sequenceService;

    private Product product, product2;

    private Purchase purchase, purchase2;

    private User user, merchant;

    private Category category;

    private String findId,
        findId2,
        userId,
        productId,
        orderId,
        purchaseId,
        purchaseId2,
        merchantId,
        categoryId,
        cartId;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);

        this.purchaseService = new PurchaseServiceImpl(purchaseRepository, productService,  userService, cartService, sequenceService);

        this.findId = "ABCD";
        this.findId2 = "id-unavailable";
        this.userId = "user-id-test";
        this.productId = "product-id-test";
        this.purchaseId = "ABCD";
        this.productId = "product-id";
        this.orderId = "order-id";
        this.purchaseId2 = "id-unavailable";
        this.merchantId = "merchant-id";
        this.cartId = "cart-id";

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

        this.merchant = User.builder()
                .description("Test Description")
                .email("test@example.com")
                .image("/api/get-img/1cab35be-cb0f-4db9-87f9-7b47db38f7ac.png")
                .name("Test Book")
                .password("Test")
                .roles(new HashSet(Collections.singleton(new Role(RoleEnum.ROLE_USER))))
                .status(true)
                .username("testusername")
                .build();

        this.merchant.setId(merchantId);

        this.category = Category.builder()
                .hidden(false)
                .image("TEST")
                .name("TEST")
                .description("TEST")
                .build();

        this.category.setId(categoryId);

        this.product = Product.builder()
                .name("string")
                .description("string")
                .price(new BigDecimal("100000"))
                .image("string")
                .pdf("test")
                .author("string")
                .isbn("test")
                .hidden(false)
                .merchant(merchant)
                .category(category)
                .build();

        this.product2 = Product.builder()
                .name("string")
                .description("string")
                .price(new BigDecimal("100000"))
                .image("string")
                .pdf("test")
                .author("string")
                .isbn("test")
                .hidden(false)
                .merchant(merchant)
                .category(category)
                .build();

        this.product.setId("test-1");
        this.product2.setImage("test-2");

        this.purchase = Purchase.builder()
                .user(user)
                .product(product)
                .merchant(merchant)
                .orderId(orderId)
                .status("WAITING")
                .build();

        this.purchase2 = Purchase.builder()
                .user(user)
                .product(product)
                .merchant(merchant)
                .orderId(orderId)
                .status("WAITING")
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
    public void testSaveRequest() {
        Set<Product> products = new HashSet<>(Arrays.asList(product, product2));
        Set<Product> cartProducts = new HashSet<>(Arrays.asList(product2, product));
        Set<String> productRequest = new HashSet<>(Arrays.asList("test-1", "test-2"));
        Cart cart = Cart.builder()
                .user(user)
                .products(cartProducts)
                .build();

        PurchaseWebRequest request = new PurchaseWebRequest(productRequest);

        when(productService.findByIdIn(productRequest)).thenReturn(products);
        when(sequenceService.save(userId.substring(0, 3).toUpperCase())).thenReturn(orderId);
        when(userService.getById(userId)).thenReturn(user);
        when(cartService.getByUserId(userId)).thenReturn(cart);
        when(purchaseRepository.existsByUserIdAndProductId(userId, productId)).thenReturn(false);
        when(purchaseRepository.save(purchase)).thenReturn(purchase);
        when(cartService.save(cart, cartId)).thenReturn(cart);

        purchaseService.save(request, userId);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testSaveRequest_NotFound() {
        Set<Product> products = new HashSet<>(Arrays.asList(product, product2));
        Set<Product> cartProducts = new HashSet<>(Collections.singletonList(product2));
        Set<String> productRequest = new HashSet<>(Arrays.asList("test-1", "test-2"));
        Cart cart = Cart.builder()
                .user(user)
                .products(cartProducts)
                .build();

        PurchaseWebRequest request = new PurchaseWebRequest(productRequest);

        when(productService.findByIdIn(productRequest)).thenReturn(products);
        when(sequenceService.save(userId.substring(0, 3).toUpperCase())).thenReturn(orderId);
        when(userService.getById(userId)).thenReturn(user);
        when(cartService.getByUserId(userId)).thenReturn(cart);
        when(purchaseRepository.existsByUserIdAndProductId(userId, productId)).thenReturn(false);
        when(purchaseRepository.save(purchase)).thenReturn(purchase);
        when(cartService.save(cart, cartId)).thenReturn(cart);

        purchaseService.save(request, userId);
    }

    @Test(expected = BadRequestException.class)
    public void testSaveRequest_BadRequest() {
        Set<Product> products = new HashSet<>(Arrays.asList(product, product2));
        Set<Product> cartProducts = new HashSet<>(Arrays.asList(product2, product));
        Set<String> productRequest = new HashSet<>(Arrays.asList("test-1", "test-2"));
        Cart cart = Cart.builder()
                .user(user)
                .products(cartProducts)
                .build();

        PurchaseWebRequest request = new PurchaseWebRequest(productRequest);

        when(productService.findByIdIn(productRequest)).thenReturn(products);
        when(sequenceService.save(userId.substring(0, 3).toUpperCase())).thenReturn(orderId);
        when(userService.getById(userId)).thenReturn(user);
        when(cartService.getByUserId(userId)).thenReturn(cart);
        when(purchaseRepository.existsByUserIdAndProductId(userId, product.getId())).thenReturn(true);
        when(purchaseRepository.save(purchase)).thenReturn(purchase);
        when(cartService.save(cart, cartId)).thenReturn(cart);

        purchaseService.save(request, userId);
    }

    @Test
    public void testGetPurchasedProduct() {
        Set<Purchase> purchases = new HashSet<>(Arrays.asList(purchase, purchase2));

        when(purchaseRepository.getAllByUserIdAndStatusIsNot(userId, "CANCEL")).thenReturn(purchases);

        assertEquals(purchases, purchaseService.getPurchasedProduct(userId));
    }

    @Test
    public void testGetByProductIdAndUserId() {
        when(purchaseRepository.getByProductIdAndUserIdAndStatusIs(productId, userId, "APPROVED")).thenReturn(purchase);

        assertEquals(purchase, purchaseService.getByProductIdAndUserId(productId, userId));
    }

    @Test
    public void testGetIncomingOrderByMerchantId_NotFound() {
        List<Purchase> purchases = Arrays.asList(purchase, purchase2);

        when(purchaseRepository.getByMerchantIdAndStatus(userId, "WAITING")).thenReturn(purchases);

        assertEquals(purchases, purchaseService.getIncomingOrderByMerchantId(userId));
    }

    @Test
    public void testApproveByOrderIdAndProductIdAndMerchantId() {
        when(purchaseRepository.findOneByOrderIdAndProductId(orderId, productId)).thenReturn(purchase);
        purchase.setStatus("APPROVED");
        when(purchaseRepository.save(purchase)).thenReturn(purchase);

        assertTrue(purchaseService.approveByOrderIdAndProductIdAndMerchantId(orderId, productId, merchantId));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testApproveByOrderIdAndProductIdAndMerchantId_NotFound() {
        purchase.getMerchant().setId("change-id");
        when(purchaseRepository.findOneByOrderIdAndProductId(orderId, productId)).thenReturn(purchase);

        assertTrue(purchaseService.approveByOrderIdAndProductIdAndMerchantId(orderId, productId, merchantId));
    }

    @Test
    public void testGetByOrderId() {
        Set<Purchase> purchases = new HashSet<>(Arrays.asList(purchase, purchase2));

        when(purchaseRepository.getAllByOrderId(orderId)).thenReturn(purchases);

        assertEquals(purchases, purchaseService.getByOrderId(orderId));
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
        when(purchaseRepository.findById(findId2)).thenReturn(Optional.empty());

        purchaseService.getById(findId2);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testEditById_NotFound(){
        when(purchaseRepository.existsById(findId2)).thenReturn(false);

        purchaseService.save(purchase2, findId2);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteById_NotFound(){
        when(purchaseRepository.existsById(findId2)).thenReturn(false);

        purchaseService.deleteById(findId2);
    }
}
