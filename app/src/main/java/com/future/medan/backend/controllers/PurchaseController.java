package com.future.medan.backend.controllers;

import com.future.medan.backend.models.entity.Purchase;
import com.future.medan.backend.payload.requests.PurchaseWebRequest;
import com.future.medan.backend.payload.requests.WebRequestConstructor;
import com.future.medan.backend.payload.responses.PurchaseWebResponse;
import com.future.medan.backend.payload.responses.Response;
import com.future.medan.backend.payload.responses.ResponseHelper;
import com.future.medan.backend.payload.responses.WebResponseConstructor;
import com.future.medan.backend.services.ProductService;
import com.future.medan.backend.services.PurchaseService;
import com.future.medan.backend.services.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api
@RestController
public class PurchaseController {

    private PurchaseService purchaseService;

    private UserService userService;

    private ProductService productService;

    @Autowired
    public PurchaseController (PurchaseService purchaseService, ProductService productService, UserService userService){
        this.purchaseService = purchaseService;
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping("/api/purchases")
    public Response<List<PurchaseWebResponse>> getAll(){
        return ResponseHelper.ok(purchaseService.getAll()
                .stream()
                .map(WebResponseConstructor::toWebResponse)
                .collect(Collectors.toList())
            );
    }

    @GetMapping("/api/purchases/{id}")
    public Response<PurchaseWebResponse> getById(@PathVariable String id){
        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(purchaseService.getById(id)));
    }

    @PostMapping(value = "/api/purchases", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<PurchaseWebResponse> save(@RequestBody PurchaseWebRequest purchaseWebRequest) {
        Purchase purchase = WebRequestConstructor.toPurchaseEntity(purchaseWebRequest);
        purchase.setUser(userService.getById(purchaseWebRequest.getUser()));
        purchase.setProduct(productService.getById(purchaseWebRequest.getProduct()));

        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(purchaseService.save(purchase)));
    }

    @PutMapping(value = "/api/purchases/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<PurchaseWebResponse> editById(@RequestBody Purchase purchase, @PathVariable String id) {
        purchase.setId(id);
        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(purchaseService.save(purchase)));
    }

    @DeleteMapping("/api/purchases/{id}")
    public void deleteById(@PathVariable String id) {
        purchaseService.deleteById(id);
    }
}
