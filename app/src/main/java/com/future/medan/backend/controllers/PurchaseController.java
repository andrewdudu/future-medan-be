package com.future.medan.backend.controllers;

import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.models.entity.Purchase;
import com.future.medan.backend.payload.requests.PurchaseWebRequest;
import com.future.medan.backend.payload.requests.WebRequestConstructor;
import com.future.medan.backend.payload.responses.*;
import com.future.medan.backend.security.JwtTokenProvider;
import com.future.medan.backend.services.ProductService;
import com.future.medan.backend.services.PurchaseService;
import com.future.medan.backend.services.SequenceService;
import com.future.medan.backend.services.UserService;
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
public class PurchaseController {

    private PurchaseService purchaseService;

    private UserService userService;

    private ProductService productService;

    private SequenceService sequenceService;

    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public PurchaseController (PurchaseService purchaseService,
                               ProductService productService,
                               UserService userService,
                               SequenceService sequenceService,
                               JwtTokenProvider jwtTokenProvider){
        this.purchaseService = purchaseService;
        this.productService = productService;
        this.userService = userService;
        this.sequenceService = sequenceService;
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

    @PostMapping(value = "/api/purchases", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<SuccessWebResponse> save(@Validated @RequestBody PurchaseWebRequest purchaseWebRequest, @RequestHeader("Authorization") String bearerToken) {
        String token = null;

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        }

        Product product = productService.getById(purchaseWebRequest.getProduct_id());
        String userId = jwtTokenProvider.getUserIdFromJWT(token);

        Purchase purchase = new Purchase();
        purchase.setUser(userService.getById(userId));
        purchase.setOrderId(sequenceService.save(userId.substring(0, 3).toUpperCase()));
        purchase.setProduct(product);
        purchase.setStatus("PENDING");
        purchase.setMerchant(product.getMerchant());

        Purchase purchaseResponse = purchaseService.save(purchase);

        return ResponseHelper.ok(new SuccessWebResponse(true));
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
