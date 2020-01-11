package com.future.medan.backend.controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.medan.backend.exceptions.ResourceNotFoundException;
import com.future.medan.backend.models.entity.Category;
import com.future.medan.backend.payload.responses.*;
import com.future.medan.backend.security.JwtTokenProvider;
import com.future.medan.backend.services.CategoryService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CategoryControllerTests {

    @Value("${local.server.port}")
    private int port;

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private CategoryService categoryService;

    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    private Category category, category2;

    private String categoryId, categoryId2, token, userId;

    @Before
    public void setup() {
        RestAssured.port = port;

        mockMvc = MockMvcBuilders.standaloneSetup(new CategoryController(categoryService)).build();

        this.userId = "user-id-test";

        this.token = Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 604800000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        this.mapper = new ObjectMapper();

        this.categoryId = "category-id-1";

        this.categoryId2 = "category-id-2";

        this.category = Category.builder()
                .name("category 1")
                .description("one")
                .image("string")
                .build();

        this.category2 = Category.builder()
                .name("category 2")
                .description("not available")
                .image("img")
                .build();

        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(userId);
        when(jwtTokenProvider.validateToken(token)).thenReturn(false);

        this.mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
    }

    @After
    public void cleanup() {
        verifyNoMoreInteractions(categoryService);
    }

    @Test
    public void testGetAll_Ok() throws Exception {
        List<Category> expected = Arrays.asList(category, category2);

        when(categoryService.getAll()).thenReturn(expected);

        Response<List<CategoryWebResponse>> response = ResponseHelper.ok(expected
                .stream()
                .map(WebResponseConstructor::toWebResponse)
                .collect(Collectors.toList()));

        mockMvc.perform(get("/api/all-categories").header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(response), json);
                });

        verify(categoryService).getAll();
    }

    @Test
    public void testGetAllWithoutHidden_Ok() throws Exception {
        List<Category> expected = Arrays.asList(category, category2);

        when(categoryService.getAllWithoutHidden()).thenReturn(expected);

        Response<List<CategoryWebResponse>> response = ResponseHelper.ok(expected
                .stream()
                .map(WebResponseConstructor::toWebResponse)
                .collect(Collectors.toList()));

        mockMvc.perform(get("/api/categories").header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(response), json);
                });

        verify(categoryService).getAllWithoutHidden();
    }

    @Test
    public void testFindPaginated_Ok() throws Exception {
        Page<Category> expected = new PageImpl<>(Arrays.asList(category, category2));

        when(categoryService.findPaginated(1, 2)).thenReturn(expected);

        PaginationResponse<List<CategoryWebResponse>> response = ResponseHelper.ok(expected
                .stream()
                .map(WebResponseConstructor::toWebResponse)
                .collect(Collectors.toList()), 2, 1);

        mockMvc.perform(get("/api/categories/paginate?page=1&size=2").header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(response), json);
                });

        verify(categoryService).findPaginated(1, 2);
    }

    @Test
    public void testGetById_Ok() throws Exception {
        Category expected = category;

        when(categoryService.getById(categoryId)).thenReturn(expected);

        Response<CategoryWebResponse> response = ResponseHelper.ok(WebResponseConstructor.toWebResponse(expected));

        mockMvc.perform(get("/api/categories/{id}", categoryId).header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(response), json);
                });

        verify(categoryService).getById(categoryId);
    }

    @Test
    public void testGetById_NotFound() throws Exception {
        when(categoryService.getById(categoryId)).thenThrow(new ResourceNotFoundException("Category", "id", categoryId));

        mockMvc.perform(get("/api/categories/{id}", categoryId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isNotFound());

        verify(categoryService).getById(categoryId);
    }

    @Test
    public void testHide_Ok() throws Exception {
        Category expected = category;

        when(categoryService.hide(categoryId)).thenReturn(expected);

        Response<CategoryWebResponse> response = ResponseHelper.ok(WebResponseConstructor.toWebResponse(expected));

        mockMvc.perform(post("/api/categories/hide/{id}", categoryId).header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(response), json);
                });

        verify(categoryService).hide(categoryId);
    }

    @Test
    public void testHide_NotFound() throws Exception {
        when(categoryService.hide(categoryId)).thenThrow(new ResourceNotFoundException("Category", "id", categoryId));

        mockMvc.perform(post("/api/categories/hide/{id}", categoryId).header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isNotFound());

        verify(categoryService).hide(categoryId);
    }

    @Test
    public void testEditById_Ok() throws Exception {
        Category expected = category;

        Response<CategoryWebResponse> response = ResponseHelper.ok(WebResponseConstructor.toWebResponse(expected));

        when(categoryService.save(category, categoryId)).thenReturn(expected);

        mockMvc.perform(put("/api/categories/{id}", categoryId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(category)))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(response), json);
                });

        verify(categoryService).save(category, categoryId);
    }

    @Test
    public void testEditById_NotFound() throws Exception {
        when(categoryService.save(category, categoryId)).thenThrow(new ResourceNotFoundException("Category", "id", categoryId));

        mockMvc.perform(put("/api/categories/{id}", categoryId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(category)))
                .andExpect(status().isNotFound());

        verify(categoryService).save(category, categoryId);
    }

    @Test
    public void testDeleteById_Ok() throws Exception {
        doNothing().when(categoryService).deleteById(categoryId);

        mockMvc.perform(delete("/api/categories/{id}", categoryId).header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk());

        verify(categoryService).deleteById(categoryId);
    }

    @Test
    public void testDeleteById_NotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Category", "id", categoryId)).when(categoryService).deleteById(categoryId);

        mockMvc.perform(delete("/api/categories/{id}", categoryId).header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isNotFound());

        verify(categoryService).deleteById(categoryId);
    }
}
