package com.future.medan.backend.controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.medan.backend.constants.ApiPath;
import com.future.medan.backend.models.entity.Category;
import com.future.medan.backend.services.CategoryService;
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
public class CategoryControllerTests {

    @Value("${local.server.port}")
    private int port;

    @MockBean
    private CategoryService service;

    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    private Category category, category2;
    private String findId, findId2;

    @Before
    public void setup() {
        RestAssured.port = port;

        mapper = new ObjectMapper();

        this.findId = "ABCD";
        this.findId2 = "id-unavailable";
        this.category = Category.builder()
                .parent_id(1)
                .name("category 1")
                .description("one")
                .image("string")
                .build();
        this.category2 = Category.builder()
                .parent_id(1)
                .name("category 2")
                .description("not available")
                .image("img")
                .build();

        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
    }

    @After
    public void cleanup() {
        verifyNoMoreInteractions(service);
    }

    @Test
    public void testGetAll() throws Exception {
        List<Category> expected = Arrays.asList(category, category2);
        List<Category> actual = service.getAll();

        when(actual).thenReturn(expected);
        mockMvc.perform(get(ApiPath.CATEGORIES))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].name", is("category 1")))
                .andExpect(jsonPath("$.data[0].image", is("string")))
                .andExpect(jsonPath("$.data[1].name", is("category 2")))
                .andExpect(jsonPath("$.data[1].image", is("img")));

        verify(service, times(1)).getAll();
    }

    @Test
    public void testGetById_OK() throws Exception {
        when(service.getById(findId)).thenReturn(category);

        mockMvc.perform(get(ApiPath.CATEGORIES + "/" + findId))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.parent_id"), is(category.getParent_id()))
                .andExpect(jsonPath("$.data.name", is(category.getName())))
                .andExpect(jsonPath("$.data.description", is(category.getDescription())))
                .andExpect(jsonPath("$.data.image", is(category.getImage())));

        verify(service, times(1)).getById(findId);
    }

    @Test
    public void testSave() throws Exception {
        when(service.save(any(Category.class))).thenReturn(category);

        mockMvc.perform(post(ApiPath.CATEGORIES)
                .content(mapper.writeValueAsString(category))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name", is(category.getName())))
                .andExpect(jsonPath("$.data.description", is(category.getDescription())))
                .andExpect(jsonPath("$.data.image", is(category.getImage())));

        verify(service, times(1)).save(any(Category.class));
    }

    @Test
    public void testEditById_OK() throws Exception {
        when(service.save(category, findId)).thenReturn(category);

        mockMvc.perform(put(ApiPath.CATEGORIES + "/" + findId)
                .content(mapper.writeValueAsString(category))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name", is(category.getName())))
                .andExpect(jsonPath("$.data.description", is(category.getDescription())))
                .andExpect(jsonPath("$.data.image", is(category.getImage())));

        verify(service, times(1)).save(category, findId);
    }

    @Test
    public void testDeleteById_Ok() throws Exception {
        doNothing().when(service).deleteById(findId);

        mockMvc.perform(delete(ApiPath.CATEGORIES + "/" + findId))
                .andExpect(status().isOk());

        verify(service, times(1)).deleteById(findId);
    }
}
