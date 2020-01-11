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
import com.future.medan.backend.security.JwtTokenProvider;
import com.future.medan.backend.services.CartService;
import com.future.medan.backend.services.ProductService;
import com.future.medan.backend.services.UserService;
import io.swagger.annotations.Api;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public Response<CartWebResponse> getByUserId(@RequestHeader("Authorization") String bearerToken) {
        String token = null;

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        }

        Cart cart = cartService.getByUserId(jwtTokenProvider.getUserIdFromJWT(token));

        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(cart));
    }

    @PostMapping(value = "/api/carts", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<CartWebResponse> save(@Validated @RequestBody CartWebRequest cartWebRequest, @RequestHeader("Authorization") String bearerToken) {
        String token = null;

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        }

        User user = userService.getById(jwtTokenProvider.getUserIdFromJWT(token));
        Product product = productService.getById(cartWebRequest.getProduct_id());

        Cart cartResponse = cartService.save(user, product);

        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(cartResponse));
    }

    // TODO: Delete this method if it's not used (Please check)
//    @PutMapping(value = "/api/carts/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//    public Response<CartWebResponse> editById(@RequestBody Cart cart, @PathVariable String id){
//        cart.setId(id);
//        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(cartService.save(cart, id)));
//    }

    @DeleteMapping("/api/carts")
    public void deleteById(@Validated @RequestBody CartWebRequest cartWebRequest, @RequestHeader("Authorization") String bearerToken) {
        String token = null;

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        }

        cartService.deleteByProductId(cartWebRequest.getProduct_id(), jwtTokenProvider.getUserIdFromJWT(token));
    }
}
