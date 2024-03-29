package com.future.medan.backend.controllers;

import com.future.medan.backend.models.entity.Cart;
import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.models.entity.User;
import com.future.medan.backend.models.entity.Wishlist;
import com.future.medan.backend.payload.requests.WishlistWebRequest;
import com.future.medan.backend.payload.responses.Response;
import com.future.medan.backend.payload.responses.ResponseHelper;
import com.future.medan.backend.payload.responses.WebResponseConstructor;
import com.future.medan.backend.payload.responses.WishlistWebResponse;
import com.future.medan.backend.security.JwtTokenProvider;
import com.future.medan.backend.services.ProductService;
import com.future.medan.backend.services.UserService;
import com.future.medan.backend.services.WishlistService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Api
@RestController
public class WishlistController {

    private WishlistService wishlistService;

    private UserService userService;

    private ProductService productService;

    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public WishlistController(WishlistService wishlistService,
                              UserService userService,
                              ProductService productService,
                              JwtTokenProvider jwtTokenProvider){
        this.wishlistService = wishlistService;
        this.userService = userService;
        this.productService = productService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/api/wishlists")
    public Response<List<WishlistWebResponse>> getAll(){
        return ResponseHelper.ok(wishlistService.getAll()
                .stream()
                .map(WebResponseConstructor::toWebResponse)
                .collect(Collectors.toList())
        );
    }

    @GetMapping("/api/wishlists/{id}")
    public Response<WishlistWebResponse> getById(@PathVariable String id){
        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(wishlistService.getById(id)));
    }

    @Transactional
    @GetMapping("/api/my-wishlists")
    public Response<WishlistWebResponse> getByUserId(@RequestHeader("Authorization") String bearerToken){
        String token = null;

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        }

        Wishlist wishlist = wishlistService.getByUserId(jwtTokenProvider.getUserIdFromJWT(token));

        System.out.println(wishlist.getUser());

        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(wishlist));
    }

    @Transactional
    @PostMapping(value = "/api/wishlists", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<WishlistWebResponse> save(@Validated @RequestBody WishlistWebRequest wishlist, @RequestHeader("Authorization") String bearerToken){
        String token = null;

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        }

        User user = userService.getById(jwtTokenProvider.getUserIdFromJWT(token));
        Product product = productService.getById(wishlist.getProduct_id());

        Wishlist wishlistResponse = wishlistService.save(user, product);

        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(wishlistResponse));
    }

    @PutMapping(value = "/api/wishlists/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<WishlistWebResponse> editById(@RequestBody Wishlist wishlist, @PathVariable String id){
        wishlist.setId(id);
        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(wishlistService.save(wishlist, id)));
    }

    @Transactional
    @DeleteMapping("/api/wishlists")
    public void deleteById(@Validated @RequestBody WishlistWebRequest wishlist, @RequestHeader("Authorization") String bearerToken){
        String token = null;

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        }

        wishlistService.deleteByProductId(wishlist.getProduct_id(), jwtTokenProvider.getUserIdFromJWT(token));
    }
}
