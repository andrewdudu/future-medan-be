package com.future.medan.backend.controllers;

import com.future.medan.backend.exceptions.ResourceNotFoundException;
import com.future.medan.backend.models.constants.ApiPath;
import com.future.medan.backend.models.entity.Purchase;
import com.future.medan.backend.responses.Response;
import com.future.medan.backend.responses.ResponseHelper;
import com.future.medan.backend.responses.WebResponseConstructor;
import com.future.medan.backend.services.PurchaseService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

@Api
@RestController
public class PurchaseController {

    private PurchaseService purchaseService;

    @Autowired
    public PurchaseController (PurchaseService purchaseService){
        this.purchaseService = purchaseService;
    }

    @GetMapping(ApiPath.PURCHASES)
    public Response getAll(){
        return ResponseHelper.ok(purchaseService.getAll()
                .stream()
                .map(WebResponseConstructor::toWebResponse)
                .collect(Collectors.toList())
            );
    }

    @GetMapping(ApiPath.PURCHASE_BY_PURCHASE_ID)
    public Response getById(@PathVariable String id){
        Optional<Purchase> purchase = purchaseService.getById(id);

        if (!purchase.isPresent())
            throw new ResourceNotFoundException("Purchase", "id", id);

        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(purchase.get()));
    }

    @PostMapping(value = ApiPath.PURCHASES, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response save(@RequestBody Purchase purchase){
        return ResponseHelper.ok(purchaseService.save(purchase));
    }

    @PutMapping(value = ApiPath.PURCHASES, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response editById(@RequestBody Purchase purchase, @PathVariable String id) {
        Optional<Purchase> findPurchase = purchaseService.getById(id);

        if (!findPurchase.isPresent())
            throw new ResourceNotFoundException("Purchase", "id", id);

        purchase.setId(id);
        return ResponseHelper.ok(purchaseService.save(purchase));
    }

    @DeleteMapping(ApiPath.PURCHASE_BY_PURCHASE_ID)
    public void deleteById(@PathVariable String id) {
        Optional<Purchase> purchase = purchaseService.getById(id);

        if (!purchase.isPresent())
            throw new ResourceNotFoundException("Purchase", "id", id);

        purchaseService.deleteById(id);
    }
}
