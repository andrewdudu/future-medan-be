package com.future.medan.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.medan.backend.models.entity.PaymentMethod;
import com.future.medan.backend.payload.responses.PaymentMethodWebResponse;
import com.future.medan.backend.payload.responses.Response;
import com.future.medan.backend.payload.responses.ResponseHelper;
import com.future.medan.backend.payload.responses.WebResponseConstructor;
import com.future.medan.backend.services.PaymentMethodService;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PaymentMethodControllerTests {

    @Value("${local.server.port}")
    private int port;

    @Mock
    private PaymentMethodService paymentMethodService;

    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    private PaymentMethod paymentMethod;

    @Before
    public void setup() {
        RestAssured.port = port;

        mockMvc = MockMvcBuilders.standaloneSetup(new PaymentMethodController(paymentMethodService)).build();

        mapper = new ObjectMapper();

        this.paymentMethod = PaymentMethod.builder()
                .active(true)
                .name("TEST")
                .type("TEST")
                .build();
    }

    @After
    public void cleanup() {
        verifyNoMoreInteractions(paymentMethodService);
    }

    @Test
    public void testGetAll() throws Exception {
        List<PaymentMethod> paymentMethods = Collections.singletonList(paymentMethod);

        Response<List<PaymentMethodWebResponse>> response = ResponseHelper.ok(paymentMethods
                .stream()
                .map(WebResponseConstructor::toPaymentMethodWebResponse)
                .collect(Collectors.toList()));

        when(paymentMethodService.getAll()).thenReturn(paymentMethods);

        mockMvc.perform(get("/api/payment-method"))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(response), json);
                });

        verify(paymentMethodService).getAll();
    }
}
