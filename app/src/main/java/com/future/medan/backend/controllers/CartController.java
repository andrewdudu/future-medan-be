package com.future.medan.backend.controllers;

import com.future.medan.backend.models.entity.Cart;
import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.models.entity.User;
import com.future.medan.backend.payload.requests.CartWebRequest;
import com.future.medan.backend.payload.responses.CartWebResponse;
import com.future.medan.backend.payload.responses.Response;
import com.future.medan.backend.payload.responses.ResponseHelper;
import com.future.medan.backend.payload.responses.WebResponseConstructor;
import com.future.medan.backend.security.JwtTokenProvider;
import com.future.medan.backend.services.CartService;
import com.future.medan.backend.services.ProductService;
import com.future.medan.backend.services.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;

@Api
@RestController
@Transactional
public class CartController {

    private CartService cartService;

    private UserService userService;

    private ProductService productService;

    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public CartController (CartService cartService,
                           UserService userService,
                           ProductService productService,
                           JwtTokenProvider jwtTokenProvider){
        this.cartService = cartService;
        this.userService = userService;
        this.productService = productService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/api/carts")
    @RolesAllowed("ROLE_USER")
    public Response<CartWebResponse> getByUserId(@RequestHeader("Authorization") String bearerToken) {
        String token = bearerToken.substring(7);

        Cart cart = cartService.getByUserId(jwtTokenProvider.getUserIdFromJWT(token));

        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(cart));
    }

    @PostMapping(value = "/api/carts", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed("ROLE_USER")
    public Response<CartWebResponse> save(@Validated @RequestBody CartWebRequest cartWebRequest, @RequestHeader("Authorization") String bearerToken) {
        String token = bearerToken.substring(7);

        User user = userService.getById(jwtTokenProvider.getUserIdFromJWT(token));
        Product product = productService.getById(cartWebRequest.getProduct_id());

        Cart cartResponse = cartService.save(user, product);

        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(cartResponse));
    }

    @DeleteMapping("/api/carts")
    @RolesAllowed("ROLE_USER")
    public void deleteById(@Validated @RequestBody CartWebRequest cartWebRequest, @RequestHeader("Authorization") String bearerToken) {
        String token = bearerToken.substring(7);

        cartService.deleteByProductId(cartWebRequest.getProduct_id(), jwtTokenProvider.getUserIdFromJWT(token));
    }
}
