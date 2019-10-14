package com.future.medan.backend.controllers;

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
import java.util.stream.Collectors;

@Api
@RestController
public class WishlistController {

    private WishlistService wishlistService;

    @Autowired
    public WishlistController(WishlistService wishlistService){
        this.wishlistService = wishlistService;
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

    @PostMapping(value = "/api/wishlists", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<WishlistWebResponse> save(@RequestBody Wishlist wishlist){
        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(wishlistService.save(wishlist)));
    }

    @PutMapping(value = "/api/wishlists/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<WishlistWebResponse> editById(@RequestBody Wishlist wishlist, @PathVariable String id){
        wishlist.setId(id);
        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(wishlistService.save(wishlist, id)));
    }

    @DeleteMapping("/api/wishlists/{id}")
    public void deleteById(@PathVariable String id){
        wishlistService.deleteById(id);
    }
}
