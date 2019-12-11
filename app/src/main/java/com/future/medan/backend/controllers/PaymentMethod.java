package com.future.medan.backend.controllers;

import com.future.medan.backend.payload.responses.PaymentMethodWebResponse;
import com.future.medan.backend.payload.responses.Response;
import com.future.medan.backend.payload.responses.ResponseHelper;
import com.future.medan.backend.payload.responses.WebResponseConstructor;
import com.future.medan.backend.services.PaymentMethodService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Api
@RestController
public class PaymentMethod {

    private PaymentMethodService paymentMethodService;

    @Autowired
    public PaymentMethod(PaymentMethodService paymentMethodService) {
        this.paymentMethodService = paymentMethodService;
    }

    @GetMapping("/api/payment-method")
    public Response<List<PaymentMethodWebResponse>> getAll() {
        return ResponseHelper.ok(paymentMethodService.getAll()
                .stream()
                .map(WebResponseConstructor::toPaymentMethodWebResponse)
                .collect(Collectors.toList()));
    }
}
