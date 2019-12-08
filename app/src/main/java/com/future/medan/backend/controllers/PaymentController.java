package com.future.medan.backend.controllers;

import com.future.medan.backend.models.entity.Purchase;
import com.future.medan.backend.payload.requests.PaymentWebRequest;
import com.future.medan.backend.payload.responses.Response;
import com.future.medan.backend.payload.responses.ResponseHelper;
import com.future.medan.backend.payload.responses.SuccessWebResponse;
import com.future.medan.backend.payload.responses.WebResponseConstructor;
import com.future.medan.backend.services.PurchaseService;
import io.swagger.annotations.Api;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.Set;

@Api
@RestController
@Transactional
public class PaymentController {

    private PurchaseService purchaseService;

    @Autowired
    public PaymentController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping("/api/payment")
    public Response<SuccessWebResponse> payment(@Validated @RequestBody PaymentWebRequest paymentWebRequest) {
        Set<Purchase> purchases = purchaseService.getByOrderId(paymentWebRequest.getOrder_id());

        purchases.forEach(purchase -> {
            purchase.setStatus("WAITING");

            purchaseService.save(purchase);
        });

        return ResponseHelper.ok(new SuccessWebResponse(true));
    }

    @PostMapping("/api/approved")
    public Response<SuccessWebResponse> approved(@Validated @RequestBody PaymentWebRequest paymentWebRequest) {
        Set<Purchase> purchases = purchaseService.getByOrderId(paymentWebRequest.getOrder_id());

        purchases.forEach(purchase -> {
            purchase.setStatus("APPROVED");

            purchaseService.save(purchase);
        });

        return ResponseHelper.ok(new SuccessWebResponse(true));
    }
}
