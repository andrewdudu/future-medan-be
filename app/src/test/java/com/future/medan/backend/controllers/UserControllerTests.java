package com.future.medan.backend.controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.medan.backend.constants.ApiPath;
import com.future.medan.backend.models.entity.User;
import com.future.medan.backend.services.UserService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerTests {
    @Value("${local.server.port}")
    private int port;

    @MockBean
    private UserService service;

    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    private User user, user2;
    private String findId, findId2;

    @Before
    public void setup() {
        RestAssured.port = port;

        mapper = new ObjectMapper();

        this.findId = "user1-id";
        this.findId2 = "id-unavailable";
        this.user = User.builder()
                .name("Andrew User")
                .description("Ini Andrew")
                .email("andrew.wijaya@gmail.com")
                .username("andrewdudu")
                .image("blekping.jpg")
                .password("dunnoWhatToD0")
                .build();
        this.user2 = User.builder()
                .name("Andrew KW")
                .description("Ini Andrew KW")
                .email("andrew.wijayaKW@gmail.com")
                .username("andrewduduKW")
                .image("redvelvet.jpg")
                .password("PeekABo0")
                .build();

        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
    }

    @After
    public void cleanup() {
        verifyNoMoreInteractions(service);
    }

    @Test
    public void testGetAll() throws Exception {
        List<User> expected = Arrays.asList(user, user2);
        List<User> actual = service.getAll();

        when(actual).thenReturn(expected);
        mockMvc.perform(get(ApiPath.USERS))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].name", is("Andrew User")))
                .andExpect(jsonPath("$.data[0].email", is("andrew.wijaya@gmail.com")))
                .andExpect(jsonPath("$.data[0].image", is("blekping.jpg")))
                .andExpect(jsonPath("$.data[1].name", is("Andrew KW")))
                .andExpect(jsonPath("$.data[1].email", is("andrew.wijayaKW@gmail.com")))
                .andExpect(jsonPath("$.data[1].image", is("redvelvet.jpg")));
        verify(service, times(1)).getAll();
    }

    @Test
    public void testGetById_OK() throws Exception {
        when(service.getById(findId)).thenReturn(user);

        mockMvc.perform(get(ApiPath.USERS + "/" + findId))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name", is(user.getName())))
                .andExpect(jsonPath("$.data.description", is(user.getDescription())))
                .andExpect(jsonPath("$.data.email", is(user.getEmail())))
                .andExpect(jsonPath("$.data.username", is(user.getUsername())))
                .andExpect(jsonPath("$.data.image", is(user.getImage())))
                .andExpect(jsonPath("$.data.password", is(user.getPassword())));

        verify(service, times(1)).getById(findId);
    }

    @Test
    public void testSave() throws Exception {
        when(service.save(any(User.class))).thenReturn(user);

        mockMvc.perform(post(ApiPath.USERS)
                .content(mapper.writeValueAsString(user))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name", is(user.getName())))
                .andExpect(jsonPath("$.data.description", is(user.getDescription())))
                .andExpect(jsonPath("$.data.email", is(user.getEmail())))
                .andExpect(jsonPath("$.data.username", is(user.getUsername())))
                .andExpect(jsonPath("$.data.image", is(user.getImage())))
                .andExpect(jsonPath("$.data.password", is(user.getPassword())));

        verify(service, times(1)).save(any(User.class));
    }

    @Test
    public void testDeleteById_Ok() throws Exception {
        doNothing().when(service).deleteById(findId);

        mockMvc.perform(delete(ApiPath.USERS + "/" + findId))
                .andExpect(status().isOk());

        verify(service, times(1)).deleteById(findId);
    }
}
