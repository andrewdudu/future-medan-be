package com.future.medan.backend.controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.medan.backend.constants.ApiPath;
import com.future.medan.backend.models.entity.Wishlist;
import com.future.medan.backend.services.WishlistService;
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

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class WishlistControllerTests {
    @Value("${local.server.port}")
    private int port;

    @MockBean
    private WishlistService service;

    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    private Wishlist wishlist, wishlist2;
    private String findId, findId2, userId1, userId2;

    @Before
    public void setup() {
        RestAssured.port = port;

        mapper = new ObjectMapper();

        this.findId = "wishlist-id-1";
        this.findId2 = "id-unavailable";
        this.userId1 = "syntialai";
        this.userId2 = "andrewwijaya";
        this.wishlist = Wishlist.builder().build();
        this.wishlist2 = Wishlist.builder().build();

        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
    }

    @After
    public void cleanup() {
        verifyNoMoreInteractions(service);
    }

    @Test
    public void testDeleteById_Ok() throws Exception {
//        doNothing().when(service).deleteById(findId);

        mockMvc.perform(delete(ApiPath.WISHLISTS + "/" + findId))
                .andExpect(status().isOk());

//        verify(service, times(1)).deleteById(findId);
    }
}
