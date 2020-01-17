package com.future.medan.backend.controllers;

import com.future.medan.backend.models.entity.Purchase;
import com.future.medan.backend.payload.requests.ApprovePurchaseWebRequest;
import com.future.medan.backend.payload.requests.PaymentWebRequest;
import com.future.medan.backend.payload.responses.Response;
import com.future.medan.backend.payload.responses.ResponseHelper;
import com.future.medan.backend.payload.responses.SuccessWebResponse;
import com.future.medan.backend.security.JwtTokenProvider;
import com.future.medan.backend.services.PurchaseService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import java.util.Set;

@Api
@RestController
@Transactional
public class PaymentController {

    private PurchaseService purchaseService;

    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public PaymentController(PurchaseService purchaseService,
                             JwtTokenProvider jwtTokenProvider) {
        this.purchaseService = purchaseService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/api/payment")
    @RolesAllowed("ROLE_ADMIN")
    public Response<SuccessWebResponse> payment(@Validated @RequestBody PaymentWebRequest paymentWebRequest) {
        Set<Purchase> purchases = purchaseService.getByOrderId(paymentWebRequest.getOrder_id());

        purchases.forEach(purchase -> {
            purchase.setStatus("WAITING");

            purchaseService.save(purchase);
        });

        return ResponseHelper.ok(new SuccessWebResponse(true));
    }

    @PostMapping("/api/approved")
    @RolesAllowed("ROLE_MERCHANT")
    public Response<SuccessWebResponse> approved(@Validated @RequestBody ApprovePurchaseWebRequest approvePurchaseWebRequest, @RequestHeader("Authorization") String bearerToken) {
        String token = bearerToken.substring(7);

        Boolean response = purchaseService.approveByOrderIdAndProductIdAndMerchantId(approvePurchaseWebRequest.getOrder_id(),
                approvePurchaseWebRequest.getProduct_id(),
                jwtTokenProvider.getUserIdFromJWT(token));


        return ResponseHelper.ok(new SuccessWebResponse(response));
    }
}
