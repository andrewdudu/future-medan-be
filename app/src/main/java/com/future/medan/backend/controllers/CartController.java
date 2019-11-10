package com.future.medan.backend.controllers;

import com.future.medan.backend.models.entity.Cart;
import com.future.medan.backend.payload.responses.Response;
import com.future.medan.backend.payload.responses.ResponseHelper;
import com.future.medan.backend.payload.responses.WebResponseConstructor;
import com.future.medan.backend.payload.responses.CartWebResponse;
import com.future.medan.backend.services.CartService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api
@RestController
public class CartController {

    private CartService cartService;

    @Autowired
    public CartController (CartService cartService){
        this.cartService = cartService;
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
    public Response<CartWebResponse> save(@RequestBody Cart cart){
        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(cartService.save(cart)));
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
