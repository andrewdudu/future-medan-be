package com.future.medan.backend.services.impl;

import com.future.medan.backend.exceptions.ResourceNotFoundException;
import com.future.medan.backend.models.entity.Category;
import com.future.medan.backend.repositories.CategoryRepository;
import com.future.medan.backend.services.CategoryService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CategoryImplTests {

    @Mock
    private CategoryRepository categoryRepository;

    private CategoryService categoryService;

    private Category category, category2;
    private String findId, findId2;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);

        this.categoryService = new CategoryServiceImpl(categoryRepository);

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
    }

    @Test
    public void testGetAll(){
        List<Category> expected = Arrays.asList(category, category2);

        when(categoryRepository.findAll()).thenReturn(expected);
        List<Category> actual = categoryService.getAll();

        assertThat(actual, is(notNullValue()));
        assertThat(actual, hasItem(category));
        assertThat(actual, hasItem(category2));
    }

    @Test
    public void testGetById_OK(){
        when(categoryRepository.findById(findId)).thenReturn(Optional.of(category2));

        assertEquals(categoryService.getById(findId), category2);
    }

    @Test
    public void testSave(){
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        assertThat(categoryService.save(category), is(notNullValue()));
    }

    @Test
    public void testEditById_OK(){
        when(categoryRepository.existsById(findId)).thenReturn(Boolean.TRUE);
        when(categoryRepository.save(category)).thenReturn(category);

        Category actual = categoryService.save(category, findId);
        assertThat(actual, is(notNullValue()));
        assertEquals(actual, category);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteById_OK(){
        when(categoryRepository.existsById(findId)).thenReturn(Boolean.TRUE);
        when(categoryRepository.findById(findId))
                .thenThrow(new ResourceNotFoundException("Category", "id", findId2));

        categoryService.deleteById(findId);
        categoryService.getById(findId2);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetById_NotFound(){
        when(categoryRepository.findById(findId2))
                .thenThrow(new ResourceNotFoundException("Category", "id", findId2));

        categoryService.getById(findId2);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testEditById_NotFound(){
        when(categoryRepository.existsById(findId2))
                .thenThrow(new ResourceNotFoundException("Category", "id", findId2));

        categoryService.save(category2, findId2);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteById_NotFound(){
        when(categoryRepository.existsById(findId2))
                .thenThrow(new ResourceNotFoundException("Category", "id", findId2));

        categoryService.deleteById(findId2);
    }
}
