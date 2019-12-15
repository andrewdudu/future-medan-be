package com.future.medan.backend.controllers;

import com.future.medan.backend.models.entity.Category;
import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.models.entity.Purchase;
import com.future.medan.backend.models.entity.User;
import com.future.medan.backend.payload.requests.ProductWebRequest;
import com.future.medan.backend.payload.requests.WebRequestConstructor;
import com.future.medan.backend.payload.responses.*;
import com.future.medan.backend.security.JwtTokenProvider;
import com.future.medan.backend.services.CategoryService;
import com.future.medan.backend.services.ProductService;
import com.future.medan.backend.services.PurchaseService;
import com.future.medan.backend.services.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Api
@RestController
public class ProductController {

    private ProductService productService;

    private CategoryService categoryService;

    private UserService userService;

    private PurchaseService purchaseService;

    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public ProductController(ProductService service,
                             CategoryService categoryService,
                             UserService userService,
                             PurchaseService purchaseService,
                             JwtTokenProvider jwtTokenProvider) {
        this.categoryService = categoryService;
        this.productService = service;
        this.userService = userService;
        this.purchaseService = purchaseService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/api/products")
    public Response<List<ProductWebResponse>> getAll() {
        return ResponseHelper.ok(productService.getAll()
                .stream()
                .map(WebResponseConstructor::toWebResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/api/my-products")
    @Transactional
    public Response<List<PurchaseWebResponse>> getMyProducts(@RequestHeader("Authorization") String bearerToken) {
        String token = null;

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        }

        String userId = jwtTokenProvider.getUserIdFromJWT(token);
        Set<Purchase> purchases = purchaseService.getPurchasedProduct(userId);

        return ResponseHelper.ok(purchases
                .stream()
                .map(WebResponseConstructor::toWebResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/api/products/{id}")
    public Response<ProductWebResponse> getById(@PathVariable String id) {
        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(productService.getById(id)));
    }

    @PostMapping(value = "/api/products/hide/{id}")
    public Response<ProductWebResponse> hideProduct(@PathVariable String id) {
        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(productService.hide(id)));
    }

    @PostMapping(value = "/api/products", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<ProductWebResponse> save(@Validated @RequestBody ProductWebRequest productWebRequest, @RequestHeader("Authorization") String bearerToken) throws IOException {
        String token = null;

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        }

        Product product = WebRequestConstructor.toProductEntity(productWebRequest);
        Category category = categoryService.getById(productWebRequest.getCategory());
        User merchant = userService.getById(jwtTokenProvider.getUserIdFromJWT(token));
        product.setCategory(category);
        product.setMerchant(merchant);

        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(productService.save(product)));
    }

    @PutMapping(value = "/api/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<ProductWebResponse> editById(@Validated @RequestBody ProductWebRequest productWebRequest, @PathVariable String id) {
        Product product = WebRequestConstructor.toProductEntity(productWebRequest);
        Category category = categoryService.getById(productWebRequest.getCategory());
        product.setCategory(category);
        product.setId(id);

        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(productService.save(product, id)));
    }

    @DeleteMapping("/api/products/{id}")
    public void deleteById(@PathVariable String id){
        productService.deleteById(id);
    }
}
