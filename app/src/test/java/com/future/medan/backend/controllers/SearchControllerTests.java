package com.future.medan.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.models.entity.Category;
import com.future.medan.backend.models.entity.Role;
import com.future.medan.backend.models.entity.User;
import com.future.medan.backend.models.enums.RoleEnum;
import com.future.medan.backend.payload.responses.ProductWebResponse;
import com.future.medan.backend.payload.responses.Response;
import com.future.medan.backend.payload.responses.ResponseHelper;
import com.future.medan.backend.payload.responses.WebResponseConstructor;
import com.future.medan.backend.services.SearchService;
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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class SearchControllerTests {

    @Value("${local.server.port}")
    private int port;

    @Mock
    private SearchService searchService;

    private ObjectMapper mapper;

    private Product product1, product2;

    private String termSuccess, termNotFound;

    @Autowired
    private MockMvc mockMvc;

    private Product product;

    @Before
    public void setup() {
        RestAssured.port = port;

        this.mockMvc = MockMvcBuilders.standaloneSetup(new SearchController(searchService)).build();

        this.mapper = new ObjectMapper();

        this.termSuccess = "Test";
        this.termNotFound = "Test2";

        User merchant = User.builder()
                .description("Test Description")
                .email("test@example.com")
                .image("/api/get-img/1cab35be-cb0f-4db9-87f9-7b47db38f7ac.png")
                .name("Test Book")
                .password("Test")
                .roles(new HashSet(Collections.singleton(new Role(RoleEnum.ROLE_USER))))
                .status(true)
                .username("testusername")
                .build();

        Category category = Category.builder()
                .hidden(false)
                .image("TEST")
                .name("TEST")
                .description("TEST")
                .build();

        this.product1 = Product.builder()
                .author("Test")
                .image("/api/get-img/1cab35be-cb0f-4db9-87f9-7b47db38f7ac.png")
                .description("Test")
                .name("Test")
                .price(new BigDecimal(123123))
                .sku("ABCD-0001")
                .pdf("HEA-0002.pdf")
                .hidden(false)
                .isbn("978-3-16-148410-0")
                .variant("ABCD-0001-0001")
                .merchant(merchant)
                .category(category)
                .build();

        this.product2 = Product.builder()
                .author("Test2")
                .image("/api/get-img/1cab35be-cb0f-4db9-87f9-7b47db38f7ac.png")
                .description("Test2")
                .name("Test2")
                .price(new BigDecimal(321321))
                .sku("ABCD-0002")
                .pdf("HEA-0003.pdf")
                .hidden(false)
                .isbn("978-3-16-148410-1")
                .variant("ABCD-0001-0002")
                .merchant(merchant)
                .category(category)
                .build();
    }

    @After
    public void cleanup() {
        verifyNoMoreInteractions(searchService);
    }

    @Test
    public void testSearch_Ok() throws Exception {
        List<Product> expected = Arrays.asList(product1, product2);

        when(searchService.search(termSuccess)).thenReturn(expected);

        Response<List<ProductWebResponse>> responseExpected = ResponseHelper.ok(expected
                .stream()
                .map(WebResponseConstructor::toWebResponse)
                .collect(Collectors.toList())
        );

        mockMvc.perform(get("/api/search?term="+termSuccess))
                .andExpect(status().isOk())
                .andDo(actual -> {
                    String responseActual = actual.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(responseExpected), responseActual);
                });

        verify(searchService).search(termSuccess);
    }
}
