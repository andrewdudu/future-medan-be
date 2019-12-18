package com.future.medan.backend.services;

import com.future.medan.backend.models.entity.PaymentMethod;

import java.util.List;

public interface PaymentMethodService {

    List<PaymentMethod> getAll();
}
