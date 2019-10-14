package com.future.medan.backend.controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.medan.backend.constants.ApiPath;
import com.future.medan.backend.models.entity.Cart;
import com.future.medan.backend.services.CartService;
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

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CartControllerTests {
    @Value("${local.server.port}")
    private int port;

    @MockBean
    private CartService service;
    
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    private Cart cart, cart2;
    private String findId, findId2;

    @Before
    public void setup() {
        RestAssured.port = port;

        mapper = new ObjectMapper();

        this.findId = "";
        this.findId2 = "id-unavailable";
        this.cart = Cart.builder().build();
//                .parent_id(1)
        this.cart2 = Cart.builder().build();
//                .parent_id(1)

        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
    }

    @After
    public void cleanup() {
        verifyNoMoreInteractions(service);
    }

    @Test
    public void testGetAll() throws Exception {
        List<Cart> expected = Arrays.asList(cart, cart2);
        List<Cart> actual = service.getAll();

        when(actual).thenReturn(expected);
        mockMvc.perform(get(ApiPath.CARTS))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].name", is("Cart 1")))
                .andExpect(jsonPath("$.data[0].image", is("string")))
                .andExpect(jsonPath("$.data[1].name", is("Cart 2")))
                .andExpect(jsonPath("$.data[1].image", is("img")));

        verify(service, times(1)).getAll();
    }

    @Test
    public void testGetById_OK() throws Exception {
        when(service.getById(findId)).thenReturn(cart);

        mockMvc.perform(get(ApiPath.CARTS + "/" + findId))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
//                .andExpect(jsonPath("$.data.parent_id"), is(Cart.getParent_id());

        verify(service, times(1)).getById(findId);
    }

    @Test
    public void testSave() throws Exception {
        when(service.save(any(Cart.class))).thenReturn(cart);

        mockMvc.perform(post(ApiPath.CARTS)
                .content(mapper.writeValueAsString(cart))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service, times(1)).save(any(Cart.class));
    }

    @Test
    public void testEditById_OK() throws Exception {
        when(service.save(cart, findId)).thenReturn(cart);

        mockMvc.perform(put(ApiPath.CARTS + "/" + findId)
                .content(mapper.writeValueAsString(cart))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service, times(1)).save(cart, findId);
    }

    @Test
    public void testDeleteById_Ok() throws Exception {
        doNothing().when(service).deleteById(findId);

        mockMvc.perform(delete(ApiPath.CARTS + "/" + findId))
                .andExpect(status().isOk());

        verify(service, times(1)).deleteById(findId);
    }
}
