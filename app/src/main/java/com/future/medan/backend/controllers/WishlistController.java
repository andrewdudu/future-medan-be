package com.future.medan.backend.controllers;

import com.future.medan.backend.exception.ResourceNotFoundException;
import com.future.medan.backend.models.constants.ApiPath;
import com.future.medan.backend.models.entity.Wishlist;
import com.future.medan.backend.responses.Response;
import com.future.medan.backend.responses.ResponseHelper;
import com.future.medan.backend.responses.WebResponseConstructor;
import com.future.medan.backend.services.WishlistService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
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

    @GetMapping(ApiPath.WISHLISTS)
    public Response getAll(){
        return ResponseHelper.ok(wishlistService.getAll()
                .stream()
                .map(WebResponseConstructor::toWebResponse)
                .collect(Collectors.toList())
        );
    }

    @GetMapping(ApiPath.WISHLIST_BY_WISHLIST_ID)
    public Response getById(@PathVariable String id){
        Optional<Wishlist> wishlist = wishlistService.getById(id);

        if (!wishlist.isPresent())
            throw new ResourceNotFoundException("Wishlist", "id", id);

        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(wishlist.get()));
    }

    @PostMapping(value = ApiPath.WISHLISTS, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response save(@RequestBody Wishlist wishlist){
        return ResponseHelper.ok(wishlistService.save(wishlist));
    }

    @PutMapping(value = ApiPath.WISHLIST_BY_WISHLIST_ID, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response editById(@RequestBody Wishlist wishlist, @PathVariable String id){
        Optional<Wishlist> findWishlist = wishlistService.getById(id);

        if (!findWishlist.isPresent())
            throw new ResourceNotFoundException("Wishlist", "id", id);

        wishlist.setId(id);
        return ResponseHelper.ok(wishlistService.save(wishlist));
    }

    @DeleteMapping(ApiPath.WISHLIST_BY_WISHLIST_ID)
    public void deleteById(String id){
        Optional<Wishlist> wishlist = wishlistService.getById(id);

        if (!wishlist.isPresent())
            throw new ResourceNotFoundException("Wishlist", "id", id);

        wishlistService.deleteById(id);
    }
}
