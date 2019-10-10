package com.future.medan.backend.services.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.medan.backend.services.ProductService;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTests {

    @Value("${local.server.port}")
    private int port;

    @MockBean
    private ProductService service;

    private ObjectMapper mapper;

    @Before
    public void setup() {
        RestAssured.port = port;

        mapper = new ObjectMapper();
    }

    @After
    public void cleanup() {
        verifyNoMoreInteractions(service);
    }
}
