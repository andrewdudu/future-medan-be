package com.future.medan.backend.controllers;

import com.future.medan.backend.payload.requests.PurchaseWebRequest;
import com.future.medan.backend.payload.responses.*;
import com.future.medan.backend.security.JwtTokenProvider;
import com.future.medan.backend.services.PurchaseService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Api
@RestController
@Transactional
public class PurchaseController {

    private PurchaseService purchaseService;

    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public PurchaseController (PurchaseService purchaseService,
                               JwtTokenProvider jwtTokenProvider){
        this.purchaseService = purchaseService;
        this.jwtTokenProvider = jwtTokenProvider;
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

    @GetMapping("/api/merchant/purchases/{id}")
    @RolesAllowed("ROLE_MERCHANT")
    public Response<List<PurchaseWebResponse>> getByMerchantId(@PathVariable String id) {
        return ResponseHelper.ok(purchaseService.getIncomingOrderByMerchantId(id)
                .stream()
                .map(WebResponseConstructor::toWebResponse)
                .collect(Collectors.toList()));
    }

    @PostMapping(value = "/api/purchases", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<SuccessWebResponse> save(@Validated @RequestBody PurchaseWebRequest purchaseWebRequest, @RequestHeader("Authorization") String bearerToken) {
        String token = bearerToken.substring(7);

        purchaseService.save(purchaseWebRequest, jwtTokenProvider.getUserIdFromJWT(token));

        return ResponseHelper.ok(new SuccessWebResponse(true));
    }

    @DeleteMapping("/api/purchases/{id}")
    public void deleteById(@PathVariable String id) {
        purchaseService.deleteById(id);
    }
}
