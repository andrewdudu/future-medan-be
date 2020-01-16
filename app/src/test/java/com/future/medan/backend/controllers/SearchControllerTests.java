package com.future.medan.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.medan.backend.models.entity.Category;
import com.future.medan.backend.models.entity.Product;
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

    @Autowired
    private MockMvc mockMvc;

    private Product product;

    @Before
    public void setup() {
        RestAssured.port = port;

        mockMvc = MockMvcBuilders.standaloneSetup(new SearchController(searchService)).build();

        mapper = new ObjectMapper();

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

        this.product = Product.builder()
                .name("string")
                .description("string")
                .price(new BigDecimal("100000"))
                .image("string")
                .pdf("test")
                .author("string")
                .isbn("test")
                .hidden(false)
                .merchant(merchant)
                .category(category)
                .build();
    }

    @After
    public void cleanup() {
        verifyNoMoreInteractions(searchService);
    }

    @Test
    public void testSearch() throws Exception {
        List<Product> products = Collections.singletonList(product);

        Response<List<ProductWebResponse>> response = ResponseHelper.ok(products
            .stream()
            .map(WebResponseConstructor::toWebResponse)
            .collect(Collectors.toList()));

        when(searchService.search("test")).thenReturn(products);

        mockMvc.perform(get("/api/search?term=test"))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(response), json);
                });

        verify(searchService).search("test");
    }
}
