package com.future.medan.backend.controllers;

import com.future.medan.backend.models.constants.ApiPath;
import com.future.medan.backend.models.entity.Category;
import com.future.medan.backend.services.CategoryService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping(ApiPath.CATEGORIES)
    public List<Category> getAll(){
        return categoryService.getAll();
    }

    @GetMapping(ApiPath.CATEGORY_BY_CATEGORY_ID)
    public Category getOne(@PathVariable String id){
        return categoryService.getOne(id);
    }

    @PostMapping(ApiPath.CATEGORIES)
    public Category save(Category category){
        return categoryService.save(category);
    }

    @DeleteMapping(ApiPath.CATEGORY_BY_CATEGORY_ID)
    public void deleteById(@PathVariable String id){
        categoryService.deleteById(id);
    }

}
