package com.future.medan.backend.controllers;

import com.future.medan.backend.models.entity.Cart;
import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.models.entity.User;
import com.future.medan.backend.payload.requests.CartWebRequest;
import com.future.medan.backend.payload.requests.WebRequestConstructor;
import com.future.medan.backend.payload.responses.Response;
import com.future.medan.backend.payload.responses.ResponseHelper;
import com.future.medan.backend.payload.responses.WebResponseConstructor;
import com.future.medan.backend.payload.responses.CartWebResponse;
import com.future.medan.backend.services.CartService;
import com.future.medan.backend.services.ProductService;
import com.future.medan.backend.services.UserService;
import io.swagger.annotations.Api;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Api
@RestController
public class CartController {

    private CartService cartService;

    private UserService userService;

    private ProductService productService;

    @Autowired
    public CartController (CartService cartService, UserService userService, ProductService productService){
        this.cartService = cartService;
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping("/api/carts")
    public Response<List<CartWebResponse>> getAll(){
        return ResponseHelper.ok(cartService.getAll()
                .stream()
                .map(WebResponseConstructor::toWebResponse)
                .collect(Collectors.toList())
        );
    }

    @GetMapping("/api/carts/{id}")
    public Response<CartWebResponse> getById(@PathVariable String id){
        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(cartService.getById(id)));
    }

    @GetMapping("/api/carts/{user_id}")
    public Response<CartWebResponse> getByUserId(@PathVariable String user_id){
        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(cartService.getByUserId(user_id)));
    }

    @PostMapping(value = "/api/carts", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<CartWebResponse> save(@Validated @RequestBody CartWebRequest cartWebRequest) {
        User user = userService.getById(cartWebRequest.getUser_id());
        Set<Product> products = productService.findByIdIn(cartWebRequest.getProduct_id());
        Cart cart = WebRequestConstructor.toCartEntity(user, products);

        Cart cartResponse = cartService.save(cart);

        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(cartResponse));
    }

    @PutMapping(value = "/api/carts/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<CartWebResponse> editById(@RequestBody Cart cart, @PathVariable String id){
        cart.setId(id);
        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(cartService.save(cart, id)));
    }

    @DeleteMapping("/api/carts/{id}")
    public void deleteById(@PathVariable String id){
        cartService.deleteById(id);
    }
}
