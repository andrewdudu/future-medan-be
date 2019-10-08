package com.future.medan.backend.controllers;

import com.future.medan.backend.exceptions.ResourceNotFoundException;
import com.future.medan.backend.models.constants.ApiPath;
import com.future.medan.backend.models.entity.Cart;
import com.future.medan.backend.responses.CartWebResponse;
import com.future.medan.backend.responses.Response;
import com.future.medan.backend.responses.ResponseHelper;
import com.future.medan.backend.responses.WebResponseConstructor;
import com.future.medan.backend.services.CartService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Api
@RestController
public class CartController {

    private CartService cartService;

    @Autowired
    public CartController (CartService cartService){
        this.cartService = cartService;
    }

    @GetMapping(ApiPath.CARTS)
    public Response<List<CartWebResponse>> getAll(){
        return ResponseHelper.ok(cartService.getAll()
                .stream()
                .map(WebResponseConstructor::toWebResponse)
                .collect(Collectors.toList())
        );
    }

    @GetMapping(ApiPath.CART_BY_CART_ID)
    public Response<CartWebResponse> getById(String id){
        Optional<Cart> cart = cartService.getById(id);

        if (!cart.isPresent())
            throw new ResourceNotFoundException("Cart", "id", id);

        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(cart.get()));
    }

    @PostMapping(value = ApiPath.CARTS, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<CartWebResponse> save(@RequestBody Cart cart){
        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(cartService.save(cart)));
    }

    @PutMapping(value = ApiPath.CART_BY_CART_ID, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<CartWebResponse> editById(@RequestBody Cart cart, @PathVariable String id){
        Optional<Cart> findCart = cartService.getById(id);

        if (!findCart.isPresent())
            throw new ResourceNotFoundException("Cart", "id", id);

        cart.setId(id);
        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(cartService.save(cart)));
    }

    @DeleteMapping(ApiPath.CART_BY_CART_ID)
    public void deleteById(@PathVariable String id){
        Optional<Cart> cart = cartService.getById(id);

        if (!cart.isPresent())
            throw new ResourceNotFoundException("Cart", "id", id);

        cartService.deleteById(id);
    }
}
