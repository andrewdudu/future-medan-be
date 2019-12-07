package com.future.medan.backend.controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.medan.backend.constants.ApiPath;
import com.future.medan.backend.models.entity.Purchase;
import com.future.medan.backend.services.PurchaseService;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PurchaseControllerTests {

    @Value("${local.server.port}")
    private int port;

    @MockBean
    private PurchaseService service;

    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    private Purchase purchase, purchase2;
    private String findId, findId2;

    @Before
    public void setup() {
        RestAssured.port = port;

        mapper = new ObjectMapper();

        this.findId = "ABCD";
        this.findId2 = "id-unavailable";
        this.purchase = Purchase.builder()
                .build();
        this.purchase2 = Purchase.builder()
                .build();

        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
    }

    @After
    public void cleanup() {
        verifyNoMoreInteractions(service);
    }

    @Test
    public void testGetAll() throws Exception {
        List<Purchase> expected = Arrays.asList(purchase, purchase2);
        List<Purchase> actual = service.getAll();

        when(actual).thenReturn(expected);
        mockMvc.perform(get(ApiPath.PURCHASES))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].productName", is("product 1")))
                .andExpect(jsonPath("$.data[0].productSku", is("sku 1")))
                .andExpect(jsonPath("$.data[0].productImage", is("img 1")))
                .andExpect(jsonPath("$.data[1].productName", is("product 2")))
                .andExpect(jsonPath("$.data[1].productSku", is("sku 2")))
                .andExpect(jsonPath("$.data[1].productImage", is("img 2")));

        verify(service, times(1)).getAll();
    }

    @Test
    public void testGetById_OK() throws Exception {
        when(service.getById(findId)).thenReturn(purchase);

        mockMvc.perform(get(ApiPath.PURCHASES + "/" + findId))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        verify(service, times(1)).getById(findId);
    }

    @Test
    public void testSave() throws Exception {
        when(service.save(any(Purchase.class))).thenReturn(purchase);

        mockMvc.perform(post(ApiPath.PURCHASES)
                .content(mapper.writeValueAsString(purchase))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service, times(1)).save(any(Purchase.class));
    }

    @Test
    public void testDeleteById_Ok() throws Exception {
        doNothing().when(service).deleteById(findId);

        mockMvc.perform(delete(ApiPath.PURCHASES + "/" + findId))
                .andExpect(status().isOk());

        verify(service, times(1)).deleteById(findId);
    }
}
