package com.future.medan.backend.services.controllers;

import com.future.medan.backend.models.entity.Purchase;
import com.future.medan.backend.payload.responses.PurchaseWebResponse;
import com.future.medan.backend.payload.responses.Response;
import com.future.medan.backend.payload.responses.ResponseHelper;
import com.future.medan.backend.payload.responses.WebResponseConstructor;
import com.future.medan.backend.services.PurchaseService;
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

    @Autowired
    public PurchaseController (PurchaseService purchaseService){
        this.purchaseService = purchaseService;
    }

    @GetMapping("/purchase")
    public Response<List<PurchaseWebResponse>> getAll(){
        return ResponseHelper.ok(purchaseService.getAll()
                .stream()
                .map(WebResponseConstructor::toWebResponse)
                .collect(Collectors.toList())
            );
    }

    @GetMapping("/purchase/{id}")
    public Response<PurchaseWebResponse> getById(@PathVariable String id){
        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(purchaseService.getById(id)));
    }

    @PostMapping(value = "/purchase", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<PurchaseWebResponse> save(@RequestBody Purchase purchase){
        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(purchaseService.save(purchase)));
    }

    @PutMapping(value = "/purchase/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<PurchaseWebResponse> editById(@RequestBody Purchase purchase, @PathVariable String id) {
        purchase.setId(id);
        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(purchaseService.save(purchase)));
    }

    @DeleteMapping("/purchase/{id}")
    public void deleteById(@PathVariable String id) {
        purchaseService.deleteById(id);
    }
}
