package com.future.medan.backend.controllers;

import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.models.entity.User;
import com.future.medan.backend.models.entity.Wishlist;
import com.future.medan.backend.payload.requests.WishlistWebRequest;
import com.future.medan.backend.payload.responses.*;
import com.future.medan.backend.security.JwtTokenProvider;
import com.future.medan.backend.services.ProductService;
import com.future.medan.backend.services.UserService;
import com.future.medan.backend.services.WishlistService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
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
    @RolesAllowed("ROLE_ADMIN")
    @Transactional
    public Response<List<WishlistWebResponse>> getAll(){
        return ResponseHelper.ok(wishlistService.getAll()
                .stream()
                .map(WebResponseConstructor::toWebResponse)
                .collect(Collectors.toList())
        );
    }

    @GetMapping(value = "/api/wishlists/paginate", params = {"page", "size"})
    @RolesAllowed("ROLE_ADMIN")
    @Transactional
    public PaginationResponse<List<WishlistWebResponse>> findPaginated(@RequestParam("page") final int page, @RequestParam("size") final int size) {
        Page<Wishlist> resultPage = wishlistService.findPaginated(page, size);

        return ResponseHelper.ok(resultPage.getContent()
                .stream()
                .map(WebResponseConstructor::toWebResponse)
                .collect(Collectors.toList()), resultPage.getTotalElements(), resultPage.getTotalPages());
    }

    @Transactional
    @GetMapping("/api/my-wishlists")
    @RolesAllowed("ROLE_USER")
    public Response<WishlistWebResponse> getByUserId(@RequestHeader("Authorization") String bearerToken){
        String token = bearerToken.substring(7);

        Wishlist wishlist = wishlistService.getByUserId(jwtTokenProvider.getUserIdFromJWT(token));

        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(wishlist));
    }

    @Transactional
    @PostMapping(value = "/api/wishlists", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed("ROLE_USER")
    public Response<WishlistWebResponse> save(@Validated @RequestBody WishlistWebRequest wishlist, @RequestHeader("Authorization") String bearerToken){
        String token = bearerToken.substring(7);

        User user = userService.getById(jwtTokenProvider.getUserIdFromJWT(token));
        Product product = productService.getById(wishlist.getProduct_id());

        Wishlist wishlistResponse = wishlistService.save(user, product);

        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(wishlistResponse));
    }

    @Transactional
    @DeleteMapping("/api/wishlists")
    public void deleteById(@Validated @RequestBody WishlistWebRequest wishlist, @RequestHeader("Authorization") String bearerToken){
        String token = bearerToken.substring(7);

        wishlistService.deleteByProductId(wishlist.getProduct_id(), jwtTokenProvider.getUserIdFromJWT(token));
    }
}
