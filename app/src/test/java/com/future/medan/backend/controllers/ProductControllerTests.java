package com.future.medan.backend.controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.medan.backend.constants.ApiPath;
import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.services.ProductService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProductControllerTests {

    @Value("${local.server.port}")
    private int port;

    @MockBean
    private ProductService service;

    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    private Product product, product2;
    private String findId, findId2;

    @Before
    public void setup() {
        RestAssured.port = port;

        mapper = new ObjectMapper();

        this.findId = "7892b1a2-0953-4071-9ffe-a5e193255585";
        this.findId2 = "id-unavailable";
        this.product = Product.builder()
                .name("string")
                .sku("string")
                .description("string")
                .price(new BigDecimal("100000"))
                .image("string")
                .author("string")
                .build();
        this.product2 = Product.builder()
                .name("my Product")
                .sku("string")
                .description("string")
                .price(new BigDecimal("0"))
                .image("img")
                .author("string")
                .build();

        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        mapper.enable(DeserializationFeature.USE_LONG_FOR_INTS);
    }

    @After
    public void cleanup() {
        verifyNoMoreInteractions(service);
    }

    @Test
    public void testGetAll() throws Exception {
        List<Product> expected = Arrays.asList(product, product2);
        List<Product> actual = service.getAll();

        when(actual).thenReturn(expected);
        mockMvc.perform(get(ApiPath.PRODUCTS))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].name", is("string")))
                .andExpect(jsonPath("$.data[0].image", is("string")))
                .andExpect(jsonPath("$.data[1].name", is("my Product")))
                .andExpect(jsonPath("$.data[1].image", is("img")));

        verify(service, times(1)).getAll();
    }

    @Test
    public void testGetById_OK() throws Exception {
        when(service.getById(findId)).thenReturn(product2);

        mockMvc.perform(get(ApiPath.PRODUCTS + "/" + findId))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name", is(product2.getName())))
                .andExpect(jsonPath("$.data.sku", is(product2.getSku())))
                .andExpect(jsonPath("$.data.description", is(product2.getDescription())))
//                .andExpect(jsonPath("$.data.price", is(product2.getPrice())))
                .andExpect(jsonPath("$.data.image", is(product2.getImage())))
                .andExpect(jsonPath("$.data.author", is(product2.getAuthor())));

        verify(service, times(1)).getById(findId);
    }

    @Test
    public void testSave() throws Exception {
        when(service.save(any(Product.class))).thenReturn(product);

        mockMvc.perform(post(ApiPath.PRODUCTS)
                .content(mapper.writeValueAsString(product))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name", is(product.getName())))
                .andExpect(jsonPath("$.data.sku", is(product.getSku())))
                .andExpect(jsonPath("$.data.description", is(product.getDescription())))
                .andExpect(jsonPath("$.data.price").value(product.getPrice()))
                .andExpect(jsonPath("$.data.image", is(product.getImage())))
                .andExpect(jsonPath("$.data.author", is(product.getAuthor())));

        verify(service, times(1)).save(any(Product.class));
    }

    @Test
    public void testEditById_OK() throws Exception {
        when(service.save(product, findId)).thenReturn(product);

        mockMvc.perform(put(ApiPath.PRODUCTS + "/" + findId)
                .content(mapper.writeValueAsString(product))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name", is(product.getName())))
                .andExpect(jsonPath("$.data.sku", is(product.getSku())))
                .andExpect(jsonPath("$.data.description", is(product.getDescription())))
                .andExpect(jsonPath("$.data.price").value(product.getPrice()))
                .andExpect(jsonPath("$.data.image", is(product.getImage())))
                .andExpect(jsonPath("$.data.author", is(product.getAuthor())));

          verify(service, times(1)).save(product, findId);
    }

    @Test
    public void testDeleteById_Ok() throws Exception {
        doNothing().when(service).deleteById(findId);

        mockMvc.perform(delete(ApiPath.PRODUCTS + "/" + findId))
                .andExpect(status().isOk());

        verify(service, times(1)).deleteById(findId);
    }
}
