package com.future.medan.backend.services.controllers;

import com.future.medan.backend.exceptions.ResourceNotFoundException;
import com.future.medan.backend.constants.ApiPath;
import com.future.medan.backend.models.entity.Wishlist;
import com.future.medan.backend.payload.responses.Response;
import com.future.medan.backend.payload.responses.ResponseHelper;
import com.future.medan.backend.payload.responses.WebResponseConstructor;
import com.future.medan.backend.payload.responses.WishlistWebResponse;
import com.future.medan.backend.services.WishlistService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Api
@RestController
public class WishlistController {

    private WishlistService wishlistService;

    @Autowired
    public WishlistController(WishlistService wishlistService){
        this.wishlistService = wishlistService;
    }

    @GetMapping("/wishlists")
    public Response<List<WishlistWebResponse>> getAll(){
        return ResponseHelper.ok(wishlistService.getAll()
                .stream()
                .map(WebResponseConstructor::toWebResponse)
                .collect(Collectors.toList())
        );
    }

    @GetMapping("/wishlists/{id}")
    public Response<WishlistWebResponse> getById(@PathVariable String id){
        Optional<Wishlist> wishlist = wishlistService.getById(id);

        if (!wishlist.isPresent())
            throw new ResourceNotFoundException("Wishlist", "id", id);

        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(wishlist.get()));
    }

    @PostMapping(value = "/wishlists", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<WishlistWebResponse> save(@RequestBody Wishlist wishlist){
        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(wishlistService.save(wishlist)));
    }

    @PutMapping(value = "/wishlists/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<WishlistWebResponse> editById(@RequestBody Wishlist wishlist, @PathVariable String id){
        Optional<Wishlist> findWishlist = wishlistService.getById(id);

        if (!findWishlist.isPresent())
            throw new ResourceNotFoundException("Wishlist", "id", id);

        wishlist.setId(id);
        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(wishlistService.save(wishlist)));
    }

    @DeleteMapping("/wishlists/{id}")
    public void deleteById(@PathVariable String id){
        Optional<Wishlist> wishlist = wishlistService.getById(id);

        if (!wishlist.isPresent())
            throw new ResourceNotFoundException("Wishlist", "id", id);

        wishlistService.deleteById(id);
    }
}
