package com.future.medan.backend.services.impl;

import com.future.medan.backend.models.entity.PaymentMethod;
import com.future.medan.backend.repositories.PaymentMethodRepository;
import com.future.medan.backend.services.PaymentMethodService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class PaymentMethodServiceImplTests {

    @Mock
    private PaymentMethodRepository paymentMethodRepository;

    private PaymentMethodService paymentMethodService;

    private PaymentMethod paymentMethod1, paymentMethod2;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        this.paymentMethodService = new PaymentMethodServiceImpl(paymentMethodRepository);

        this.paymentMethod1 = new PaymentMethod();
        this.paymentMethod2 = new PaymentMethod();

        paymentMethod1.setName("OVO");
        paymentMethod1.setType("Virtual");
        paymentMethod1.setActive(true);

        paymentMethod2.setName("Gopay");
        paymentMethod1.setType("Virtual");
        paymentMethod1.setActive(true);
    }

    @Test
    public void testGetAll() {
        List<PaymentMethod> expected = Arrays.asList(paymentMethod1, paymentMethod2);
        when(paymentMethodRepository.findAll()).thenReturn(expected);

        List<PaymentMethod> actual = paymentMethodService.getAll();

        assertEquals(expected, actual);
    }
}
